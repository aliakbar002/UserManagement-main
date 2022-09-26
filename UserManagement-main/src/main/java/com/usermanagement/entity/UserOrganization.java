package com.usermanagement.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_organization")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class UserOrganization implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "status")
    private int status;

    @ManyToOne//user side ManyToMany
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne//Organization side many to many
    @JoinColumn(name = "organization_id")
    private Organization organization;


    @ManyToOne
    @JoinColumn(name = "organization_role_id")
    private OrganizationRole organizationRole;

    public UserOrganization(int status, User user, Organization organization, OrganizationRole organizationRole) {
        this.status = status;
        this.user = user;
        this.organization = organization;
        this.organizationRole = organizationRole;
    }
}
