package be.aplacetolive.entity;

import be.aplacetolive.entity.types.TypeActivite;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Created by Medard on 12/05/2017.
 */

@Entity
@Table(name = "activites")
public class Activite implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeActivite type;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;

    @Column(name = "lieu", nullable = false)
    private String lieu;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "participations")
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeActivite getType() {
        return type;
    }

    public void setType(TypeActivite type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
