package com.mrInstruments.backend.service;

import com.mrInstruments.backend.entities.Stock;
import com.mrInstruments.backend.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock saveStock(Stock stock){
        return stockRepository.save(stock);
    }

//    public Optional<Stock> buscarStockPorCategoria(Categoria categoria){
//        return stockRepository.findByCategoria(categoria);
//    }

    public List<Stock> listStock(){
        return stockRepository.findAll();
    }
}
