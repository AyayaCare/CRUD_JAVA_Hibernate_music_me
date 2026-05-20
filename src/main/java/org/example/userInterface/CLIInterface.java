package org.example.userInterface;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import org.example.service.SongService;
import org.example.entity.ArtistEntity;
import org.example.entity.SongsEntity;
import org.example.service.ArtistService;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class CLIInterface {

    @Getter
    private static boolean flag = true;

    public static void mainMenu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("====== Welcome to the CLI interface for your music! ======");
        while (flag) {
            System.out.println();
            System.out.println();
            System.out.println(" |||| Please, select an option: ||||");
            System.out.println("1.Create new data about a song" + "\n" + "2.Update data about a song" +
                    "\n" + "3.Get a list with alL songs" + "\n" + "4.Filter a song by ID" + "\n"
             + "5.Filter a song by name" + "\n" +  "6.Filter artist"+ "\n" + "7.Add artist"+ "\n" + "8.Delete data" +
            "\n" + "9.Exit");
            int selection = scan.nextInt();
            scan.nextLine();
            if (selection > 9) {
                continue;
            }
            if (selection == 9) {
                System.out.println("See u! <3");
                flag = false;
                continue;

            }

                switch (selection) {

                    case 1:
                        //Esto me sirve para aumentar el valor de un entero, lo cual no se permite en lambdas
                        //con enteros primitivos
                        AtomicInteger number = new AtomicInteger(1);
                        SongsEntity song = new SongsEntity();
                        System.out.println("Please, write the data: ");
                        System.out.println("Song name: ");
                        String name = scan.nextLine().toLowerCase(Locale.ROOT);
                        song.setSong_name(name);
                        System.out.println("Done!");
                        System.out.println("Now, please, select the artist");
                        System.out.println("Search: ");

                        String searchedArtist = scan.nextLine().toLowerCase(Locale.ROOT);

                        List<ArtistEntity> listOfArtist = ArtistService.searchArtist(searchedArtist);

                        if (listOfArtist.isEmpty()) {
                            System.out.println("No artist found with that name");
                            break;
                        }


                        listOfArtist.stream().map((ArtistEntity::toString))
                                .forEach((result) ->
                                        System.out.println(number.getAndIncrement() + "." + result));


                        System.out.println();

                        System.out.println("Is there the artist you searched for? yes/no ");
                        String confirmation = scan.nextLine().toLowerCase(Locale.ROOT);
                        if ("yes".equalsIgnoreCase(confirmation)) {
                            System.out.println("Write the index of the artist: ");
                            int indexSelected = scan.nextInt() - 1;
                            scan.nextLine();
                            if (indexSelected < 0 || indexSelected >= listOfArtist.size()){
                                System.out.println("Invalid selection");
                                break;
                            }
                            ArtistEntity artist = listOfArtist.get(indexSelected);
                            song.setArtist(artist);
                            System.out.println("Done!");
                            System.out.println("Please, write the song's duration: ");
                            int writtenDuration = scan.nextInt();
                            scan.nextLine();
                            Long finalDuration = Long.valueOf(writtenDuration);
                            song.setDuration(finalDuration);
                            System.out.println("Write the year");
                            int writtenYear = scan.nextInt();
                            scan.nextLine();
                            Integer finalYear = Integer.valueOf(writtenYear);
                            song.setYear(finalYear);
                            SongService.save(song);

                        }

                         else if ("no".equalsIgnoreCase(confirmation)) {

                        }

                        else {
                        System.out.println("Please, only 'yes' or 'no'");
                    }
                        break;

                    case 2:
                        System.out.println("Write the song ID");
                        int id;
                        if(scan.hasNextInt()) {
                            id = scan.nextInt();

                            SongService.updateData(Long.valueOf(id));

                        } else {
                            System.out.println("You have to digit a valid ID");
                            scan.nextLine();

                        }
                        break;

                    case 3:
                        List <SongsEntity> listOfAllSongs = SongService.findAll();
                        listOfAllSongs.stream().forEach(System.out::println);

                        break;

                    case 4:
                        System.out.println("Please, digit the song's ID: ");
                        int selection2 = scan.nextInt();
                        scan.nextLine();
                        Long newSelection = Long.valueOf(selection2);

                        SongsEntity returned = SongService.findByID(newSelection);
                        System.out.println(returned.toString());

                        break;

                    case 5:
                        System.out.println("Write the song name: ");
                        String selection3 = scan.nextLine().toLowerCase(Locale.ROOT);
                        if (selection3 != null) {
                            SongService.findSongByName(selection3);


                        } else {
                            System.out.println("You should write a song name");
                        }
                        break;

                    case 6:
                        System.out.println("Write the artist name");
                        String selection4 = scan.nextLine().toLowerCase(Locale.ROOT);
                        if (selection4.isEmpty()) {
                            System.out.println("You should write a name");
                            continue;
                        }
                        List<ArtistEntity> artists = ArtistService.searchArtist(selection4);
                        if (artists != null &&
                            !artists.isEmpty()) {


                            for (ArtistEntity a : artists) {
                                System.out.println(a);
                            }

                        } else {
                            System.out.println("Doesn't found");

                        }
                        break;

                    case 7:
                        ArtistEntity artist = new ArtistEntity();
                        System.out.println("We'll guide you!");

                        System.out.println("Write the artist name: ");
                        String artistName = scan.nextLine().toLowerCase(Locale.ROOT);

                        List<ArtistEntity> artistlist = ArtistService.searchArtist(artistName);
                        if (!artistlist.isEmpty()) {
                            AtomicInteger i = new AtomicInteger(1);
                            System.out.println("We found this artists with a similar name: ");
                            for (ArtistEntity l : artistlist) {

                                System.out.println(i.getAndIncrement()+ "." + l.getArtist_name());

                            }

                                System.out.println("What do you want to do?");
                                System.out.println("Write '1' if you want to use one of them or '0' if you want to create new data");
                                int selection5;
                                if (scan.hasNextInt()) {
                                    selection5 = scan.nextInt();
                                    scan.nextLine();

                                    if (selection5 < 0) {
                                        System.out.println("Please, only options listed");
                                    } else if (selection5 > 1) {
                                        System.out.println("Please, only options listed");
                                    } else if (selection5 == 0) {
                                        artist.setArtist_name(artistName);
                                        ArtistService.addArtist(artist);
                                        System.out.println("Done! Artist data created");


                                    } else if (selection5 == 1) {
                                        System.out.println("You can use one of the options listed in the " +
                                                "main menu, where you can search for artist and their music");



                                    }
                                } else {
                                    System.out.println("Please, enter a number");
                                    scan.nextLine();

                                }
                            } else {
                            artist.setArtist_name(artistName);
                            ArtistService.addArtist(artist);
                            System.out.println("Done! Data has been created");
                        }
                        break;

                    case 8:

                        ArtistService.deleteData();
                        break;

                    default:
                        System.out.println("Invalid option");

                }



            System.out.println("\nPlease, ENTER to continue");
            scan.nextLine();

            }



        }

}
