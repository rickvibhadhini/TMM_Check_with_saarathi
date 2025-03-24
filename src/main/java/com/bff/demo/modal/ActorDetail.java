package com.bff.demo.modal;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ActorDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String actorId;
    private String actorRole;
    private String actorType;
}
