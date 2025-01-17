package com.lantu;

import com.lantu.service.INewbarcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class FileWatcherService {
    
    private Path path;
    
    @Autowired
    private INewbarcodeService newbarcodeService;
    
    @Autowired
    private WebSocketServicer webSocketServicer;  // 注入WebSocket通知服务
    
    
    public FileWatcherService() {
        // 默认路径，可以在应用启动时进行设置
        this.path = Paths.get("path/to/directory");
    }
    
    public void setDirectoryPath(String directoryPath) {
        this.path = Paths.get(directoryPath);
    }
    
    public void start() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            System.out.println("开始监听文件夹: " + path);
            
            while (true) {
                WatchKey key;
                try {
                    key = watchService.poll(2, TimeUnit.SECONDS);
                    if (key == null) {
                        continue;
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
                
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE && fileName.toString().endsWith(".txt")) {
                        System.out.println("新增txt文件: " + fileName);
                        Path filePath = path.resolve(fileName);
                        
                        // 延迟读取，确保文件完全写入
                        try {
                            Thread.sleep(500);  // 等待500毫秒
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        
                        try {
                            List<String> lines = readAllLinesWithRetries(filePath, 3, 500);
                            if (lines == null) {
                                System.err.println("无法读取文件内容: " + filePath);
                                continue;
                            }
                            
                            System.out.println("文件内容:");
                            for (String line : lines) {
                                System.out.println(line);
                            }
                            List<String> newLines = lines.stream()
                                    .map(line -> {
                                        if (line != null) {
                                            return line.trim();  // 去除前后空白字符，包括制表符
                                        } else {
                                            return null;
                                        }
                                    })
                                    .collect(Collectors.toList());
                            
                            // 调用业务逻辑处理文件内容
                            newbarcodeService.processFileContent(newLines);
                            // 向前端发送通知
                            webSocketServicer.sendToAllClient("新文件处理完成: " + fileName);
    
                        } catch (IOException e) {
                            System.err.println("无法读取文件内容: " + e.getMessage());
                        }
                    }
                }
                
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }
    
    private List<String> readAllLinesWithRetries(Path path, int retries, int delayMillis) throws IOException {
        for (int i = 0; i < retries; i++) {
            try {
                return Files.readAllLines(path, StandardCharsets.UTF_8);
            } catch (IOException e) {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Interrupted while waiting to retry file read", interruptedException);
                }
            }
        }
        return null;
    }
}
