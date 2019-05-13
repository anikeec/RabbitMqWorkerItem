/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqworkeritem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author apu
 * this is worker function with delay for imitation difficult process
 */
public class WorkerSymbolCounter {
    
    private Map<Character,Integer> symbolMap = new HashMap<>();
    
    private final int AMOUNT_OF_SYMBOLS_BETWEEN_DELAYS = 1000;
    private final int THREAD_DELAY_MS = 50;
    
    public Map<Character,Integer> countSymbols(String text) {
        
        symbolMap.clear();
       
        Character symbol;
        int amount = 0;
        for(int i=0; i<text.length(); i++) {
            if(i%AMOUNT_OF_SYMBOLS_BETWEEN_DELAYS == 0) {
                try {
                    Thread.sleep(THREAD_DELAY_MS);
                } catch (InterruptedException ex) {}
            }
            symbol = text.charAt(i);            
            if(symbolMap.containsKey(symbol)) {
                amount = symbolMap.get(symbol);                              
            } else {
                amount = 0;
            }
            amount++;  
            symbolMap.put(symbol, amount);
        }
        
        return symbolMap;
    }
    
    public String getStringResults(Map<Character,Integer> symbolMap) {
        Set<Map.Entry<Character, Integer>> set = symbolMap.entrySet();
        Iterator<Map.Entry<Character, Integer>> it = set.iterator();
        Map.Entry<Character, Integer> entry;
        StringBuilder sb = new StringBuilder();
        while(it.hasNext()) {
            entry = it.next();
            sb.append(entry.getKey())
                    .append(" - ")
                    .append(entry.getValue())
                    .append("\n");            
        }
        return sb.toString();
    }
    
}
