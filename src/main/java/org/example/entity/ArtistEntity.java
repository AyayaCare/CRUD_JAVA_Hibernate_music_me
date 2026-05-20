package org.example.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable = false)
    @Getter
    @Setter
    private String artist_name;

    //“mappedBy= No crees otra tabla intermedia, ya existe la FK en songs”
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    //Sin esta lista, puedo ir de songs a artist, pero no de artist a songs
    //por eso es necesaria, así el one puede acceder a sus many
    private List<SongsEntity> songs = new ArrayList<>();

    /*
    protected Artist() {}

    public Artist(String name) {
        this.name = name;
    }*/

    public void addSong(SongsEntity song) {
        songs.add(song);
        song.setArtist(this);
    }

    public void removeSong(SongsEntity song) {
        songs.remove(song);
        song.setArtist(null);
    }

    @Override
    public String toString() {
        return artist_name + " (ID: " + id + ")";
    }

}
