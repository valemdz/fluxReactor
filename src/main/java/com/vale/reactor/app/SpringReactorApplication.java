package com.vale.reactor.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vale.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringReactorApplication implements CommandLineRunner{
	
	Logger logger = LoggerFactory.getLogger(SpringReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Flux< String > cadenas = Flux.just("Dani Sultano","Sandra Mengano","Pepita Fulano","Vale Torres" ); 
				
				
		Flux< Usuario > usuarios = cadenas.map( nombre -> new Usuario( nombre.toUpperCase().split(" ")[0],
																	   nombre.toUpperCase().split(" ")[1] ) )
								   .doOnNext( usuario ->{										   
											   if( usuario == null ) {
												   throw new RuntimeException("Los nombre no pueden estar vacios");
											   }											   
											   System.out.println( usuario );
								   			  })
								   .map( usuario -> { usuario.setNombre( usuario.getNombre().toLowerCase());
									   				  return usuario; })
								   .filter( usr -> usr.getNombre().length() > 4 ); 
		//Sin onComplete
		//cadenas.subscribe( logger::info , err -> logger.error( err.getMessage() ) );
		
		//Con onComplete
		
		usuarios.subscribe( usuario -> logger.info(usuario.toString() ), 
						   err -> logger.error( err.getMessage() ),
						   new Runnable() {
							
							@Override
							public void run() {
								logger.info("Se mostraron los nombre con exito!!!");
								
							}
						});
		
	}

}
