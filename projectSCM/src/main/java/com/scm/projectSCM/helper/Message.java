package com.scm.projectSCM.helper;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {
    String content;
    @Builder.Default
    MessageType type = MessageType.blue;
}
