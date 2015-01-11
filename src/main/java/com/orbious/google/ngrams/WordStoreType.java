package com.orbious.google.ngrams;

public enum WordStoreType {
  NOUN, //nouns 
  ADJ,  //adjectives
  VERB, //verbs
  ADV,  //adverbs
  PRON, //pronouns
  DET,  //determiners and articles 
  CONJ, //conjunctions
  ADP,  //prepositions and postpositions
  NUM,  //numerals
  PRT,  //particles
  X;    //catchall

  public static String toString(WordStoreType type) {
    switch ( type ) {
    case NOUN:
      return "noun";
    case ADJ:
      return "adj";
    case VERB:
      return "verb";
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
    case "adj":
      return WordStoreType.ADJ;
    case "verb":
      return WordStoreType.VERB;
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
    case "x":
      return WordStoreType.X;
    default:
      return null;
    }
  }
  
}
