package br.com.tiago.schermack.projeto_teste_automatizado.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeRequestDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.dto.EmployeeResponseDTO;
import br.com.tiago.schermack.projeto_teste_automatizado.entity.Employee;
import br.com.tiago.schermack.projeto_teste_automatizado.repository.EmployeeRepository;

@SpringBootTest
@Transactional
public class EmployeeServiceIntegracaoTeste {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void deveCriarFuncionarioNoBanco() {
        // Arrenge
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO("Joao", "joao@gmail.com");                                                                                         // do banco

        // Act
        EmployeeResponseDTO responseDTO = employeeService.create(employeeRequestDTO);

        // Assert
        assertEquals(1L, responseDTO.id());
        assertEquals("Joao", responseDTO.firstName());
        assertEquals("joao@gmail.com", responseDTO.email());
    }

    @Test
     public void deveAtualizarFuncionarioComSucesso(){

        //Arrange
         EmployeeRequestDTO requestDTO = new EmployeeRequestDTO("joao", "joao@gmail.com");  
         Employee employeeSaved = new Employee("pedro", "pedro@gmail");
         
         employeeRepository.save(employeeSaved); //Vou estar salvando meu novo funcionário

         employeeRepository.findById(employeeSaved.getId());

        //Act
        EmployeeResponseDTO responseDTO = employeeService.update(employeeSaved.getId(), requestDTO); // Busca o funcionário no Service com o id e atualiza ele

        //Assert
        assertEquals(1L, responseDTO.id());
        assertEquals("joao", responseDTO.firstName());
        assertEquals("joao@gmail.com", responseDTO.email());

    }

    @Test
    public void deveDeletarFuncionario(){
        // Arrange
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO("Joao", "joao@gmail.com");                                                                                         // do banco
        EmployeeResponseDTO responseDTO = employeeService.create(employeeRequestDTO);
        
        //Act
        employeeService.delete(responseDTO.id());                                                   

        Employee responseUpdated = employeeRepository.findById(responseDTO.id()).orElse(null);
        
        //Assert
        assertNull(responseUpdated);
    }

}
