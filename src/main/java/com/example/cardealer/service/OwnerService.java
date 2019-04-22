package com.example.cardealer.service;

import com.example.cardealer.mappers.OwnerMapper;
import com.example.cardealer.model.Owner;
import com.example.cardealer.model.dtos.OwnerDto;
import com.example.cardealer.repository.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
    private OwnerRepository ownerRepository;
    private OwnerMapper ownerMapper;


    public OwnerService(OwnerRepository ownerRepository,
                        OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    public Owner addOwner(OwnerDto ownerDto) {
        return ownerRepository.save(ownerMapper.reverse(ownerDto));
    }

    public Owner getOwnerById(Integer ownerId) {
        return ownerRepository.getOne(ownerId);
    }

    public void updateOwner(OwnerDto ownerDto) {
        ownerRepository.findById(ownerDto.getOwnerId())
                .ifPresent(o -> {
                    o.setAddress(ownerDto.getAddress());
                    o.setFirstName(ownerDto.getFirstName());
                    o.setLastName(ownerDto.getLastName());
                    o.setPesel(ownerDto.getPesel());
                    o.setPhoneNumber(ownerDto.getPhoneNumber());
                    o.setTin(ownerDto.getTin());
                    ownerRepository.save(o);
                });
    }
}
