package com.orbious.google.ngrams;

public enum WordStoreType {
  NOUN,
  VERB,
  ADJ,
  ADV,
  PRON,
  DET,
  CONJ,
  ADP,
  NUM,
  PRT,
  ROOT,
  START,
  END,
  X;

  public static String toString(WordStoreType type) {
    switch ( type ) {
    case NOUN:
      return "noun";
    case VERB:
      return "verb";
    case ADJ:
      return "adj";
    case ADV:
      return "adv";
    case PRON:
      return "pron";
    case DET:
      return "det";
    case CONJ:
      return "conj";
    case ADP:
      return "adp";
    case NUM:
      return "num";
    case PRT:
      return "prt";
    case ROOT:
      return "root";
    case START:
      return "start";
    case END:
      return "end";
    case X:
      return "x";
    default:
      return null;
    }
  }

  public static WordStoreType fromString(String str) {
    switch ( str.toLowerCase() ) {
    case "noun":
      return WordStoreType.NOUN;
    case "verb":
      return WordStoreType.VERB;
    case "adj":
      return WordStoreType.ADJ;
    case "adv":
      return WordStoreType.ADV;
    case "pron":
      return WordStoreType.PRON;
    case "det":
      return WordStoreType.DET;
    case "conj":
      return WordStoreType.CONJ;
    case "adp":
      return WordStoreType.ADP;
    case "num":
      return WordStoreType.NUM;
    case "prt":
      return WordStoreType.PRT;
    case "root":
      return WordStoreType.ROOT;
    case "start":
      return WordStoreType.START;
    case "end":
      return WordStoreType.END;
    case "x":
      return WordStoreType.X;
    default:
      return null;
    }
  }

}
