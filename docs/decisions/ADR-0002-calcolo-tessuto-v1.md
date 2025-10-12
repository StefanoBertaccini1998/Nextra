# ADR 0002 — Calcolo Tessuto v1
Data: 2025-10-12 — Stato: Approvato

## Contesto
Stima metri di tessuto per ordine in base a modello, altezza rotolo, sfrido.

## Decisione
- Tabella parametri per modello (pannelli standard)
- Input: altezza rotolo (es. 140/160 cm), optional (bracciolo)
- Formula v1:
  `mt_richiesti = (somma_pannelli / altezza_rotolo_cm) * fattore_sfrido`
- Persistenza in `order_item_requirements`

## Alternative
Motore regole (Drools) in v2.

## Conseguenze
Implementazione rapida e testabile; si affina in v2.
