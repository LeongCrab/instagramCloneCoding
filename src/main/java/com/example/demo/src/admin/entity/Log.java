package com.example.demo.src.admin.entity;

import com.example.demo.common.Constant.DataType;
import com.example.demo.common.Constant.MethodType;
import com.example.demo.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private DataType dataType;

    @Column(nullable = false)
    private MethodType methodType;

    @Column(nullable = false)
    private Long userId;
}
