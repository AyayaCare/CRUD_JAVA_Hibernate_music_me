package org.example.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor()
@Table(name = "songs")
@Setter
@Getter
public class SongsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String song_name;

    private Long duration;

    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist")
    private ArtistEntity artist;


    /*
    *     protected Song() {}

    public Song(String title, int duration, Artist artist) {
        this.title = title;
        this.duration = duration;
        this.artist = artist;
    * */

    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Title: " + song_name +"\n" + "ID:" + id + "\n" + "Duration: " + duration + "\n" + "Year: " + year;
    }

}


