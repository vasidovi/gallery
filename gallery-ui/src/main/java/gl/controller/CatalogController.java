package gl.controller;

import gl.model.entity.CatalogEntity;
import gl.service.CatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogController {

    private CatalogService service;

    public CatalogController(CatalogService service) {
        this.service = service;
    }

    @GetMapping("/catalogs")
    public List<CatalogEntity> getAllCatalogs() {
        return service.findAll();
    }

}
