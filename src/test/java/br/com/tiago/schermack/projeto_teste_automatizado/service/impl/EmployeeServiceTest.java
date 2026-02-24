package br.com.tiago.schermack.projeto_teste_automatizado.service.impl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeRequestDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeResponseDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.entity.Employee;
import br.com.tiago.schermack.projeto_teste_automatizado.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;

    @SpringBootTest
    class EmployeeServiceTest 
    {

    @InjectMocks
    private EmployeeService employeeService; 

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("Esse teste vai ser responsável por validar a criação do funcionário")
    public void deveCriarEmpregadoERetornarResponseDTO(){

        //Arrange
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("Joao","joao@gmail.com");
        Employee employeeSaved = new Employee(requestDTO.firstName(), requestDTO.email());
        employeeSaved.setId(1L);

        when(employeeRepository.save(any(Employee.class)))
        .thenReturn(employeeSaved);

        //Act
        EmployeeResponseDTO responseDTO = employeeService.create(requestDTO);


        //Assert
        assertEquals(1L, responseDTO.id());
        assertEquals("Joao", responseDTO.firstName());
        assertEquals("joao@gmail.com", responseDTO.email());

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    
    //update
    @Test
    public void deveAtualizarFuncionarioComSucesso(){

        //Arrange
         EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("joao", "joao@gmail.com"); // Vou estatar atualizando as informações de Pedro que esta no banco 
         Employee employeeSaved = new Employee("pedro", "pedro@gmail");                     // e atualizando para joao e seu email, pois pedro saiu do sistema
         employeeSaved.setId(1L);

         when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeSaved)); //Quando o service chama findById(1L) e devolve esse funcionário

        //Act
        EmployeeResponseDTO responseDTO = employeeService.update(1L, requestDTO); // Busca o funcionário no Service com o id(1) e atualiza ele (João)

        //Assert
        assertEquals(1L, responseDTO.id());
        assertEquals("joao", responseDTO.firstName());
        assertEquals("joao@gmail.com", responseDTO.email());

    }

    @Test
    public void deveLancarQuandoNaoExistir(){ //Quando não existir deve lançar uma resposta de erro 
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("joao", "joao@gmail.com");
        
         when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

         // Act + Assert
        assertThrows(EntityNotFoundException.class, () ->
        employeeService.update(1L, requestDTO));

           verify(employeeRepository).findById(1L);

    }

    @Test
    public void deveDeletarFuncionarioExistente(){ //Vai estar cadastrando um funcionário, buscando por ID e deletando ele do sistema
        //Arrange
        Employee employee = new Employee("Joao", "joao@gmail.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        //Act
         employeeService.delete(1L); 
        //Assert
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    } 
    
}