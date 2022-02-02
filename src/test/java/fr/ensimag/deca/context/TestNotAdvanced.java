package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.Not;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;


/**
 * Test for the Not node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2022
 */

public class TestNotAdvanced {
    
    final Type BOOLEAN = new BooleanType(null);

    @Mock
    AbstractExpr boolexpr1;

    DecacCompiler compiler;

    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null, null);
        when(boolexpr1.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
    }

    @Test
    public void testBool () throws ContextualError {
        Not t = new Not (boolexpr1) ;
        
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
    
        verify(boolexpr1).verifyExpr(compiler, null, null);
    }


}
