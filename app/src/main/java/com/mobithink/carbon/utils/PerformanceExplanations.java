package com.mobithink.carbon.utils;

import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.TripDTO;

/**
 * Created by mplaton on 22/03/2017.
 */

public class PerformanceExplanations {

    EventDTO eventDTO;
    String eventExplanations;

    public String performanceExplanations(TripDTO tripDTO){

        if (eventDTO.getStationName() == null){

            switch (eventDTO.getEventName()) {
                case "Giration difficile":
                    eventExplanations = "Impact: Inconfort des voyageurs à bords (secousses), ralentissement du bus" +
                            "Aménagement à privilégier: reprise d'aménagement ";
                    break;

                case "Voie étroite":
                    eventExplanations = "Impact: Largeur trop réduite des voies de circulation" +
                            "Aménagement à privilégier: Élargissement des voies pour permettre le croisement de deux bus ";
                    break;

                case "Chicane, écluse":
                    eventExplanations = "Impact: Inconfort des voyageurs à bords (secousses), risques d'accrochages" +
                            "Aménagement à privilégier: Coussin berlinois (voire plateau surélevé plus long) ";
                    break;

                case "Dos d'âne, trapézoïdal":
                    eventExplanations = "Impact: Dégradation du châssis du véhicule et du fonctionnement de la palette PMR, inconfort des voyageurs à bord(secousses)" +
                            "Aménagement à privilégier: Coussin berlinois (voire plateau surélevé plus long) ";
                    break;

                case "Pavé trop rugueux":
                    eventExplanations = "Impact: Dégradation des suspensions du véhicule, inconfort des voyageurs à bord (secousses), nuisances sonores pour les voyageurs et les riverains" +
                            "Aménagement à privilégier: Pavés plus lisses (type granit) ";
                    break;

                case "Stationnement latéral":
                    eventExplanations = "Impact: Ralentissement du véhicule, par perception de danger potentiel par le chauffeur en porésence de stationnement transversal à 90° ou produisant un effet d'étroitesse" +
                            "Aménagement à privilégier: Suppression du stationnement latéral, aménagement de couloirs bus";
                    break;

                case "Stationnement illicite":
                    eventExplanations = "Impact: Ralentissement par nécessité pour le bus de changer de file de circulation, risque d’accident avec un véhicule stationné et/ou avec les autres modes en circulation" +
                            "Aménagement à privilégier: Contrôle renforcé du stationnement, aménagement de couloir bus";
                    break;

                case "Stationnement alterné (effet chicane)":
                    eventExplanations = "Impact: Inconfort des voyageurs à bords (secousses), risques d'accrochages" +
                            "Aménagement à privilégier: Coussin berlinois, plateau surélevé plus long";
                    break;

                case "Passage à niveau":
                    eventExplanations = "Impact: Attente du passage du train" +
                            "Aménagement à privilégier: Nouvel itinéraire, passage inférieur sous voie-ferrée";
                    break;

                case "Itinéraire en tiroir ou boucle":
                    eventExplanations = "Impact: Rallongement inutile du temps de parcours, inconfort des voyageurs à bord, ressenti de perte de temps" +
                            "Aménagement à privilégier: Suppression du tiroir ou de la boucle";
                    break;

                case "Itinéraire sinueux":
                    eventExplanations = "Impact: Itinéraire rendu sinueux par le plan de circulation retardant le bus par rapport au trajet naturel" +
                            "Aménagement à privilégier: Reprise du plan de circulation avec des actions favorisant le bus";
                    break;

                case "Trafic":
                    eventExplanations = "Impact: Congestion routière" +
                            "Aménagement à privilégier: Aménagement de couloirs bus";
                    break;

                case "Panne":
                    eventExplanations = "Impact: Changement de véhicule";
                    break;

                case "Giratoire : remontée des files d'attente":
                    eventExplanations = "Impact: Ralentissement du véhicule, dégradation des consommations" +
                            "Aménagement à privilégier: Couloir d'approche d'accès aux carrefours, by-pass";
                    break;

                case "Giratoire : attente":
                    eventExplanations = "Impact: Ralentissement du véhicule, dégradation des consommations" +
                            "Aménagement à privilégier: Conception du giratoire pour prioriser la branche, mise en place d'un carrefour à feux";
                    break;

                case "Giratoire : giration trop importante":
                    eventExplanations = "Impact: Ralentissement du véhicule, inconfort des voyageurs" +
                            "Aménagement à privilégier: Création d’une voie spécifique pour les bus sur les giratoires à grand rayon, quand le bus sort à la sortie suivante (voie « by-pass »), réaménagement du gabarit du carrefour (élargissement du rayon de giration ou aménagement d’îlots centraux pour empêcher le stationnement ventouse), percement du giratoire et insertion d'un site propre avec gestion du carrefour avec des feux";
                    break;

                case "Carrefour à feux : remontée des files d'attente":
                    eventExplanations = "Impact: Ralentissement du véhicule, dégradation des consommations" +
                            "Aménagement à privilégier: Couloir d'approche d'accès aux carrefours combiné à une priorité bus aux feux ou à une simple anticipation de phase pour favoriser le passage du bus dès le premier cycle du feu en cours, by-pass pour tourne à gauche";
                    break;

                case "Carrefour à feux : attente":
                    eventExplanations = "Impact: Ralentissement du véhicule, dégradation des consommations" +
                            "Aménagement à privilégier: Recalage des phases pour prioriser l'axe emprunté par le bus, priorité bus aux feux";
                    break;

                case "Carrefour à feux : étroit car îlot refuge":
                    eventExplanations = "Impact: Ralentissement du véhicule";
                    break;

                case "Carrefour à feux : ligne de feu trop avancée":
                    eventExplanations = "Impact: Zone d’attente devant un feu d’une voie transversale de faible largeur positionnée trop près du feu : impossibilité de croisement de deux véhicules lors de la giration au carrefour. Risque d’accident avec un véhicule au feu rouge.Congestion du carrefour." +
                            "Aménagement à privilégier: Déplacement de la ligne de feux plus en amont du carrefour pour permettre la giration des bus arrivant en sens inverse";
                    break;

                default:
                    break;

            }
        } else {
            switch (eventDTO.getEventName()) {
                case "Trop d'arrêts":
                    eventExplanations = "Impact: Les arrêts trop proches les uns des autres nuisent au temps de parcours global de la ligne" +
                            "Aménagement à privilégier: Suppression d'arrêts trop proches les uns des autres avec un objectif de distance d'interstation minimale";
                    break;

                case "Stationnement illicite":
                    eventExplanations = "Impact: Un stationnement illicite sur l'aire d'arrêt peut entrâiner un accostage difficile aux points d’arrêt ne permettant pas l’accessibilité pour les PMR" +
                            "Aménagement à privilégier: Contrôle du stationnement, mise en avancée des points d’arrêt, aménagement d'aires de livraison (les stations de bus sont souvent occupées pour cela)";
                    break;

                case "Attente pour correspondance":
                    eventExplanations = "Impact: Prise de retard du bus" +
                            "Aménagement à privilégier: Reprise de l'exploitation";
                    break;

                case "Capacité station":
                    eventExplanations = "Impact: Trop de véhicule en même temps à la station, mauvaise conditions de montées/descentes pour les voyageurs" +
                            "Aménagement à privilégier: Reprise de l'exploitation, augmentation des capacités d'accueil du pôle";
                    break;

                case "Foule":
                    eventExplanations = "Impact: Trop de voyageurs en même temps à la station en hyper-pointe (souvent scolaires), mauvaise conditions de montées/descentes pour les voyageurs et risque élevé d'accidents" +
                            "Aménagement à privilégier: Reprise de l'exploitation et injection de services supplémentaires, nouvel aménagement de la station";
                    break;

                case "Incivilité":
                    eventExplanations = "Impact: Incivilité ou problème quelconque avec un ou plusieurs voyageurs" +
                            "Aménagement à privilégier: Mesures de prévention";
                    break;

                case "Demande d'informations à bord":
                    eventExplanations = "Impact: Prise de retard du bus" +
                            "Aménagement à privilégier: Mettre en place en station, l'information voyageurs nécessaire, s'assurer de la facile compréhension des informations (plan, FH …), traduction des informations";
                    break;

                case "Vente à bord et échange de monnaie":
                    eventExplanations = "Impact: Prise de retard du bus (monnaie)" +
                            "Aménagement à privilégier: Mettre en place en station les automates";
                    break;

                case "Réinsertion dans la circulation":
                    eventExplanations = "Impact: Attente réinsertion du bus dans la circulation" +
                            "Aménagement à privilégier: Mise en avancée des points d’arrêt ou arrêt en ligne";
                    break;

                case "PMR":
                    eventExplanations = "Impact: Prise en charge d'une personne à mobilité réduite : fauteuil roulant, poussette essentiellement" +
                            "Aménagement à privilégier: La mise en accessibilité des points d'arrêts et des matriels est logiquement programmée, voire réalisée. Pas de gain de temps envisageable.";
                    break;

                case "Incident technique":
                    eventExplanations = "Impact: Incident technique bus (porte, traction, palette rétractable)" +
                            "Aménagement à privilégier: Si nombreux, problème de maintenance des matériels";
                    break;

                default:
                    break;
            }
        }

        return eventExplanations;
    }
}
