package com.example.demo.src.admin.entity;

import com.example.demo.common.Constant.DataType;
import com.example.demo.common.Constant.MethodType;
import com.example.demo.common.entity.BaseEntity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "LOG")
public class Log extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataType dataType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MethodType methodType;

    @Column(nullable = false)
    private Long userId;

    public Log(DataType dataType, MethodType methodType, Long userId){
        this.dataType = dataType;
        this.methodType = methodType;
        this.userId = userId;
    }
}
