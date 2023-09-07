package com.mrInstruments.backend.controller;

import com.mrInstruments.backend.entities.Stock;
import com.mrInstruments.backend.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> listarProductos() {
        List<Stock> stock = stockService.listStock();
        return ResponseEntity.ok(stock);
    }
}
