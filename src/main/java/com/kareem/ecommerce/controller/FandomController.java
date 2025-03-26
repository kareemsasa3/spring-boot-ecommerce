package com.kareem.ecommerce.controller;

import com.kareem.ecommerce.model.Fandom;
import com.kareem.ecommerce.model.dto.FandomDTO;
import com.kareem.ecommerce.service.FandomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fandoms")
public class FandomController {

    private final FandomService fandomService;

    public FandomController(FandomService fandomService) {
        this.fandomService = fandomService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Fandom>> getAllFandoms() {
        List<Fandom> fandoms = fandomService.getAllFandoms();
        return ResponseEntity.ok(fandoms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fandom> getFandomById(@PathVariable Long id) {
        Optional<Fandom> fandom = fandomService.getFandomById(id);
        return fandom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Fandom> createFandom(@RequestBody FandomDTO fandomDTO) {
        Fandom fandom = new Fandom();
        fandom.setName(fandomDTO.getName());
        fandom.setDescription(fandomDTO.getDescription());

        fandom.setProducts(new ArrayList<>());

        Fandom createdFandom = fandomService.createFandom(fandom);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFandom);
    }

    @PatchMapping("/{fandomId}/add-product/{productId}")
    public ResponseEntity<?> addProductToFandom(@PathVariable Long fandomId, @PathVariable Long productId) {
        fandomService.addProductToFandom(fandomId, productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fandom> updateFandom(@PathVariable Long id, @RequestBody Fandom fandomDetails) {
        Fandom updatedFandom = fandomService.updateFandom(id, fandomDetails);
        if (updatedFandom != null) {
            return ResponseEntity.ok(updatedFandom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFandom(@PathVariable Long id) {
        fandomService.deleteFandom(id);
        return ResponseEntity.noContent().build();
    }
}
