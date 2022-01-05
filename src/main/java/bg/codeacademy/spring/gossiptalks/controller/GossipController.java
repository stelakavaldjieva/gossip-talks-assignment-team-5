package bg.codeacademy.spring.gossiptalks.controller;

import bg.codeacademy.spring.gossiptalks.dto.gossip.CreateGossipRequest;
import bg.codeacademy.spring.gossiptalks.dto.gossip.GossipDTO;
import bg.codeacademy.spring.gossiptalks.dto.gossip.GossipListDTO;
import bg.codeacademy.spring.gossiptalks.model.Gossip;
import bg.codeacademy.spring.gossiptalks.service.GossipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/v1")
public class GossipController
{

  private final GossipService gossipService;

  public GossipController(GossipService gossipService)
  {
    this.gossipService = gossipService;
  }

  @GetMapping("/gossips")
  public ResponseEntity<GossipListDTO> readAllGossips(@Min(0) @RequestParam(defaultValue = "0") Integer pageNo,
                                                      @Min(1) @RequestParam(defaultValue = "20") Integer pageSize)
  {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<Gossip> pageOfGossip = gossipService.getAllSubscriberGossips(pageable);
    //Map page to gossipListDTO.
    GossipListDTO gossipListDTO = mapGossipToGossipListDTO(pageNo, pageSize, pageOfGossip);

    return ResponseEntity.ok(gossipListDTO);
  }
  {}
  @GetMapping("/users/{username}/gossips")
  public ResponseEntity<GossipListDTO> readUserGossips(@Min(0) @RequestParam(defaultValue = "0") Integer pageNo,
                                                       @Min(1) @RequestParam(defaultValue = "20") Integer pageSize,
                                                       @PathVariable String username)
  {
    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<Gossip> pageOfGossip = gossipService.getByUser(username, pageable);
    //Map page to gossipListDTO.
    GossipListDTO gossipListDTO = mapGossipToGossipListDTO(pageNo, pageSize, pageOfGossip);

    return ResponseEntity.ok(gossipListDTO);
  }


  @PostMapping(value = "/gossips", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<GossipDTO> createGossip(@Valid @ModelAttribute CreateGossipRequest createGossipRequest)
  {
    Gossip gossip = gossipService.createGossip(
        createGossipRequest.getText());

    GossipDTO gossipDTO = new GossipDTO(
        Integer.toString(gossip.getId().intValue(), 36),
        gossip.getText(),
        gossip.getUser().getUsername(),
        gossip.getDatetime());

    return ResponseEntity.ok(gossipDTO);
  }

  private GossipListDTO mapGossipToGossipListDTO(Integer pageNo, Integer pageSize, Page<Gossip> pageOfGossip)
  {
    return new GossipListDTO(
        pageNo,
        pageSize,
        pageOfGossip.getNumberOfElements(),
        (int) pageOfGossip.getTotalElements(),

        //Map the gossip list to content list of gossip dto.
        pageOfGossip.getContent()
            .stream()
            .map(gossip -> new GossipDTO(
                Integer.toString(gossip.getId().intValue(), 36),
                gossip.getText(),
                gossip.getUser().getUsername(),
                gossip.getDatetime()
            )).collect(
                Collectors.toList()));
  }
}
