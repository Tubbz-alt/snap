package org.snapscript.parse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class TokenIndexerTest extends TestCase {
   
   public void testTokenIndexer() throws Exception {
      List<Token> tokens = createTokens("return 1+2;");
      
      assertEquals(tokens.get(0).getType(), TokenType.LITERAL.mask);
      assertEquals(tokens.get(1).getType(), TokenType.SPACE.mask);
      assertEquals(tokens.get(2).getType(), TokenType.DECIMAL.mask);
   }
   
   private List<Token> createTokens(String text) {
      List<Token> tokens = new ArrayList<Token>();
      GrammarIndexer grammarIndexer = new GrammarIndexer();
      Map<String, Grammar> grammars = new LinkedHashMap<String, Grammar>();      
      GrammarResolver grammarResolver = new GrammarResolver(grammars);
      GrammarCompiler grammarCompiler = new GrammarCompiler(grammarResolver, grammarIndexer);  
      SourceProcessor sourceProcessor = new SourceProcessor(100);
      Syntax[] language = Syntax.values();
      
      for(Syntax syntax : language) {
         String name = syntax.getName();
         String value = syntax.getGrammar();
         Grammar grammar = grammarCompiler.process(name, value);
         
         grammars.put(name, grammar);
      }
      SourceCode source = sourceProcessor.process(text);
      char[] original = source.getOriginal();
      char[] compress = source.getSource();
      short[] lines = source.getLines();
      short[]types = source.getTypes();

      TokenIndexer tokenIndexer = new TokenIndexer(grammarIndexer, text, original, compress, lines, types);
      tokenIndexer.index(tokens);
      return tokens;
   }

}
