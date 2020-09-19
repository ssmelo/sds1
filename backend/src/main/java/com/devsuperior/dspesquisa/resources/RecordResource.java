package com.devsuperior.dspesquisa.resources;

import com.devsuperior.dspesquisa.dto.GameDTO;
import com.devsuperior.dspesquisa.dto.RecordDTO;
import com.devsuperior.dspesquisa.dto.RecordInsertDTO;
import com.devsuperior.dspesquisa.entities.Record;
import com.devsuperior.dspesquisa.services.GameService;
import com.devsuperior.dspesquisa.services.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/records")
public class RecordResource {

    @Autowired
    private RecordService recordService;

    @PostMapping
    public ResponseEntity<RecordDTO> insert(@RequestBody RecordInsertDTO dto){
        RecordDTO newDTO = recordService.insert(dto);
        return ResponseEntity.ok().body(newDTO);

    }

    @GetMapping
    public ResponseEntity<Page<RecordDTO>> findAll(
        @RequestParam(value = "min", defaultValue = "") String min,
        @RequestParam(value = "max", defaultValue = "") String max,
        @RequestParam(value = "linesPerPage", defaultValue = "0") Integer linesPerPage,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "orderBy", defaultValue = "moment") String orderBy,
        @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ){
        Instant minDate = min.equals("") ? null : Instant.parse(min);
        Instant maxDate = max.equals("") ? null : Instant.parse(max);

        if(linesPerPage == 0){
            linesPerPage = Integer.MAX_VALUE;
        }

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        Page<RecordDTO> list = recordService.findByMoments(minDate,maxDate, pageRequest);
        return ResponseEntity.ok().body(list);
    }
}
