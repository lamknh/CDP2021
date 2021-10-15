package cse.knu.cdp1.service;

import cse.knu.cdp1.dto.StoringDTO;

import java.util.List;


public interface StoringService {
    public abstract List<StoringDTO> storingList();
    public abstract void storingInsert(StoringDTO insertData);
    public abstract void storingDelete(StoringDTO deleteData);
}
