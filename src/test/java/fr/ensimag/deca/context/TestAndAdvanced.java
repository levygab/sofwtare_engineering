package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.And;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;


/**
 * Test for the And node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class TestAndAdvanced {

    final Type BOOLEAN = new BooleanType(null);
 

    @Mock
    AbstractExpr boolexpr1;
    @Mock
    AbstractExpr boolexpr2;

    DecacCompiler compiler;
    
    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.openMocks(this);
        compiler = new DecacCompiler(null, null);
        when(boolexpr1.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);
        when(boolexpr2.verifyExpr(compiler, null, null)).thenReturn(BOOLEAN);

    }

    @Test
    public void testBoolBool() throws ContextualError {
        And t = new And(boolexpr1, boolexpr2);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isBoolean());
        // check that the mocks have been called properly.
        verify(boolexpr1).verifyExpr(compiler, null, null);
        verify(boolexpr2).verifyExpr(compiler, null, null);
    }



}