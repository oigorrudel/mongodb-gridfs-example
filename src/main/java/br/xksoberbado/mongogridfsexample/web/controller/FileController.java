package br.xksoberbado.mongogridfsexample.web.controller;

import br.xksoberbado.mongogridfsexample.service.FileService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService service;

    @GetMapping("{name}")
    public GridFSFile get(@PathVariable final String name) {
        return service.getByName(name);
    }

    @SneakyThrows
    @GetMapping(value = "{name}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> download(@PathVariable final String name) {
        final var file = service.download(name);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + file.getFilename())
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(file.contentLength())
            .body(file);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String upload(@RequestParam final MultipartFile file) {
        service.create(file);

        return file.getName();
    }

    @DeleteMapping("{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final String name) {
        service.delete(name);
    }
}
