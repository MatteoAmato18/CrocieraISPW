package entity;

/**
 * Stato di disponibilità di una cabina.
 * FREE        = stanza disponibile (default)<br>
 * PENDING    = prenotata ma non ancora confermata dalla compagnia<br>
 * OCCUPATED  = prenotazione confermata / stanza occupata
 *
 * (Ho mantenuto la grafia “OCCUPATED” per rispettare la richiesta,
 * ma ti consiglio “OCCUPIED” se preferisci l’inglese corretto.)
 */
public enum RoomStatus {
    FREE,
    PENDING,
    OCCUPATED
}
