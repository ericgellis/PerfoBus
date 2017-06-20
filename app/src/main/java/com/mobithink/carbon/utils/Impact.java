package com.mobithink.carbon.utils;

import com.mobithink.carbon.database.model.EventDTO;

/**
 * Created by mplaton on 22/03/2017.
 */

public class Impact {

    String eventExplanations;

    public String impactExplanations(EventDTO eventDTO){

        if (eventDTO.getStationName() == null){

            switch (eventDTO.getEventName()) {
                case "Giration difficile":
                    eventExplanations = "Inconfort des voyageurs à bords (secousses), ralentissement du bus.";
                    break;

                case "Voie étroite":
                    eventExplanations = "Largeur trop réduite des voies de circulation.";
                    break;

                case "Chicane, écluse":
                    eventExplanations = "Inconfort des voyageurs à bords (secousses), risques d'accrochages.";
                    break;

                case "Dos d'âne, trapézoïdal":
                    eventExplanations = "Dégradation du châssis du véhicule et du fonctionnement de la palette PMR, inconfort des voyageurs à bord(secousses)." ;
                    break;

                case "Pavé trop rugueux":
                    eventExplanations = "Dégradation des suspensions du véhicule, inconfort des voyageurs à bord (secousses), nuisances sonores pour les voyageurs et les riverains.";
                    break;

                case "Stationnement latéral":
                    eventExplanations = "Ralentissement du véhicule, par perception de danger potentiel par le chauffeur en porésence de stationnement transversal à 90° ou produisant un effet d'étroitesse." ;
                    break;

                case "Stationnement illicite":
                    eventExplanations = "Ralentissement par nécessité pour le bus de changer de file de circulation, risque d’accident avec un véhicule stationné et/ou avec les autres modes en circulation.";
                    break;

                case "Stationnement alterné (effet chicane)":
                    eventExplanations = "Inconfort des voyageurs à bords (secousses), risques d'accrochages.";
                    break;

                case "Passage à niveau":
                    eventExplanations = "Attente du passage du train.";
                    break;

                case "Itinéraire en tiroir ou boucle":
                    eventExplanations = "Rallongement inutile du temps de parcours, inconfort des voyageurs à bord, ressenti de perte de temps.";
                    break;

                case "Itinéraire sinueux":
                    eventExplanations = "Itinéraire rendu sinueux par le plan de circulation retardant le bus par rapport au trajet naturel.";
                    break;

                case "Trafic":
                    eventExplanations = "Congestion routière." ;
                    break;

                case "Panne":
                    eventExplanations = "Changement de véhicule.";
                    break;

                case "Giratoire : remontée des files d'attente":
                    eventExplanations = "Ralentissement du véhicule, dégradation des consommations.";
                    break;

                case "Giratoire : attente":
                    eventExplanations = "Ralentissement du véhicule, dégradation des consommations." ;
                    break;

                case "Giratoire : giration trop importante":
                    eventExplanations = "Ralentissement du véhicule, inconfort des voyageurs.";
                    break;

                case "Carrefour à feux : remontée des files d'attente":
                    eventExplanations = "Ralentissement du véhicule, dégradation des consommations." ;
                    break;

                case "Carrefour à feux : attente":
                    eventExplanations = "Ralentissement du véhicule, dégradation des consommations." ;
                    break;

                case "Carrefour à feux : étroit car îlot refuge":
                    eventExplanations = "Ralentissement du véhicule.";
                    break;

                case "Carrefour à feux : ligne de feu trop avancée":
                    eventExplanations = "Zone d’attente devant un feu d’une voie transversale de faible largeur positionnée trop près du feu : impossibilité de croisement de deux véhicules lors de la giration au carrefour. Risque d’accident avec un véhicule au feu rouge.Congestion du carrefour.";
                    break;

                default:
                    break;

            }
        } else {
            switch (eventDTO.getEventName()) {
                case "Trop d'arrêts":
                    eventExplanations = "Les arrêts trop proches les uns des autres nuisent au temps de parcours global de la ligne." ;
                    break;

                case "Stationnement illicite":
                    eventExplanations = "Un stationnement illicite sur l'aire d'arrêt peut entrâiner un accostage difficile aux points d’arrêt ne permettant pas l’accessibilité pour les PMR.";
                    break;

                case "Attente pour correspondance":
                    eventExplanations = "Prise de retard du bus.";
                    break;

                case "Capacité station":
                    eventExplanations = "Trop de véhicule en même temps à la station, mauvaise conditions de montées/descentes pour les voyageurs.";
                    break;

                case "Foule":
                    eventExplanations = "Trop de voyageurs en même temps à la station en hyper-pointe (souvent scolaires), mauvaise conditions de montées/descentes pour les voyageurs et risque élevé d'accidents.";

                    break;

                case "Incivilité":
                    eventExplanations = "Incivilité ou problème quelconque avec un ou plusieurs voyageurs.";
                    break;

                case "Demande d'informations à bord":
                    eventExplanations = "Prise de retard du bus.";
                    break;

                case "Vente à bord et échange de monnaie":
                    eventExplanations = "Prise de retard du bus (monnaie).";
                    break;

                case "Réinsertion dans la circulation":
                    eventExplanations = "Attente réinsertion du bus dans la circulation." ;
                    break;

                case "PMR":
                    eventExplanations = "Prise en charge d'une personne à mobilité réduite : fauteuil roulant, poussette essentiellement.";
                    break;

                case "Incident technique":
                    eventExplanations = "Incident technique bus (porte, traction, palette rétractable).";
                    break;

                default:
                    break;
            }
        }

        return eventExplanations;
    }
}
