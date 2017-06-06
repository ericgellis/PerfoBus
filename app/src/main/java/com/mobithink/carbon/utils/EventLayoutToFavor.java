package com.mobithink.carbon.utils;

import com.mobithink.carbon.database.model.EventDTO;

/**
 * Created by mplaton on 06/06/2017.
 */

public class EventLayoutToFavor {

    String eventLayoutToFavor;

    public String eventLayoutToFavor(EventDTO eventDTO){

        if (eventDTO.getStationName() == null){

            switch (eventDTO.getEventName()) {
                case "Giration difficile":
                    eventLayoutToFavor = " Reprise d'aménagement ";
                    break;

                case "Voie étroite":
                    eventLayoutToFavor = " Élargissement des voies pour permettre le croisement de deux bus ";
                    break;

                case "Chicane, écluse":
                    eventLayoutToFavor = " Coussin berlinois (voire plateau surélevé plus long) ";
                    break;

                case "Dos d'âne, trapézoïdal":
                    eventLayoutToFavor = " Coussin berlinois (voire plateau surélevé plus long) ";
                    break;

                case "Pavé trop rugueux":
                    eventLayoutToFavor = " Pavés plus lisses (type granit)";
                    break;

                case "Stationnement latéral":
                    eventLayoutToFavor = " Suppression du stationnement latéral, aménagement de couloirs bus";
                    break;

                case "Stationnement illicite":
                    eventLayoutToFavor = " Contrôle renforcé du stationnement, aménagement de couloir bus";
                    break;

                case "Stationnement alterné (effet chicane)":
                    eventLayoutToFavor = " Coussin berlinois, plateau surélevé plus long";
                    break;

                case "Passage à niveau":
                    eventLayoutToFavor = " Nouvel itinéraire, passage inférieur sous voie-ferrée";
                    break;

                case "Itinéraire en tiroir ou boucle":
                    eventLayoutToFavor = " Suppression du tiroir ou de la boucle";
                    break;

                case "Itinéraire sinueux":
                    eventLayoutToFavor = " Reprise du plan de circulation avec des actions favorisant le bus";
                    break;

                case "Trafic":
                    eventLayoutToFavor ="  Aménagement de couloirs bus.";
                    break;

                case "Panne":
                    eventLayoutToFavor = " ";
                    break;

                case "Giratoire : remontée des files d'attente":
                    eventLayoutToFavor = " Couloir d'approche d'accès aux carrefours, by-pass.";
                    break;

                case "Giratoire : attente":
                    eventLayoutToFavor = " Conception du giratoire pour prioriser la branche, mise en place d'un carrefour à feux.";
                    break;

                case "Giratoire : giration trop importante":
                    eventLayoutToFavor = " Création d’une voie spécifique pour les bus sur les giratoires à grand rayon, quand le bus sort à la sortie suivante (voie « by-pass »), réaménagement du gabarit du carrefour (élargissement du rayon de giration ou aménagement d’îlots centraux pour empêcher le stationnement ventouse), percement du giratoire et insertion d'un site propre avec gestion du carrefour avec des feux.";
                    break;

                case "Carrefour à feux : remontée des files d'attente":
                    eventLayoutToFavor = " Couloir d'approche d'accès aux carrefours combiné à une priorité bus aux feux ou à une simple anticipation de phase pour favoriser le passage du bus dès le premier cycle du feu en cours, by-pass pour tourne à gauche.";
                    break;

                case "Carrefour à feux : attente":
                    eventLayoutToFavor = " Recalage des phases pour prioriser l'axe emprunté par le bus, priorité bus aux feux.";
                    break;

                case "Carrefour à feux : étroit car îlot refuge":
                    eventLayoutToFavor = " ";
                    break;

                case "Carrefour à feux : ligne de feu trop avancée":
                    eventLayoutToFavor = " Déplacement de la ligne de feux plus en amont du carrefour pour permettre la giration des bus arrivant en sens inverse.";
                    break;

                default:
                    break;

            }
        } else {
            switch (eventDTO.getEventName()) {
                case "Trop d'arrêts":
                    eventLayoutToFavor = " Suppression d'arrêts trop proches les uns des autres avec un objectif de distance d'interstation minimale.";
                    break;

                case "Stationnement illicite":
                    eventLayoutToFavor = " Contrôle du stationnement, mise en avancée des points d’arrêt, aménagement d'aires de livraison (les stations de bus sont souvent occupées pour cela).";
                    break;

                case "Attente pour correspondance":
                    eventLayoutToFavor = " Reprise de l'exploitation.";
                    break;

                case "Capacité station":
                    eventLayoutToFavor = " Reprise de l'exploitation, augmentation des capacités d'accueil du pôle.";
                    break;

                case "Foule":
                    eventLayoutToFavor = " Reprise de l'exploitation et injection de services supplémentaires, nouvel aménagement de la station.";
                    break;

                case "Incivilité":
                    eventLayoutToFavor = " Mesures de prévention.";
                    break;

                case "Demande d'informations à bord":
                    eventLayoutToFavor = " Mettre en place en station, l'information voyageurs nécessaire, s'assurer de la facile compréhension des informations (plan, FH …), traduction des informations.";
                    break;

                case "Vente à bord et échange de monnaie":
                    eventLayoutToFavor = " Mettre en place en station les automates.";
                    break;

                case "Réinsertion dans la circulation":
                    eventLayoutToFavor = " Mise en avancée des points d’arrêt ou arrêt en ligne.";
                    break;

                case "PMR":
                    eventLayoutToFavor = " La mise en accessibilité des points d'arrêts et des matriels est logiquement programmée, voire réalisée. Pas de gain de temps envisageable.";
                    break;

                case "Incident technique":
                    eventLayoutToFavor = " Si nombreux, problème de maintenance des matériels.";
                    break;

                default:
                    break;
            }
        }

        return eventLayoutToFavor;
    }
}
