
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nomina</title>
	<asset:javascript src="forms/forms.js"/>
</head>
<body>
	<lx:header>
		<div class="col-md-10">
			<g:link action="show" id="${nomina.id}">
				<h2>Nómina: ${nomina.tipo} ${nomina.folio} <small>(${nomina.periodicidad} / ${nomina.formaDePago})</small> </h2>
			</g:link>
			<g:if test="${flash.message}">
            	<lx:warningLabel></lx:warningLabel>
        	</g:if> 
        	
		</div>
	</lx:header>

	<div class=" row wrapper wrapper-content  animated fadeInRight">
		<div class="col-md-6 col-md-offset-3">
			<lx:ibox>
				<lx:iboxTitle title="Propiedades"/>
					<g:form action="saveNominaDeAsimilado" class="form-horizontal"  >
						<lx:iboxContent>
							<lx:errorsHeader bean="${command}"></lx:errorsHeader>
							<f:with bean="${command }">
								<g:hiddenField name="nomina.id" value="${nomina.id}"/>
								<f:field property="asimilado" widget-class="form-control" label="Persona">
									<g:select class="form-control chosen"  id="asimiladoField"
										name="asimilado" 
										from="${asimilados}" 
										optionKey="id" 
										optionValue="nombre"
										noSelection="[null:'Persona o empleado asimilado a salarios  ']"
									/>
								</f:field>
								<f:field property="importe" widget="numeric"/>
							</f:with>
							

						</lx:iboxContent>
						<lx:iboxFooter>
							<div class=" btn-group">
				    			<lx:backButton  action="show" id="${nomina.id}"></lx:backButton>
				      			<button type="submit" class="btn btn-primary btn-outline">
				      				<span class="glyphicon glyphicon-floppy-save"></span> Agregar
				      			</button>
				    		</div>
						</lx:iboxFooter>
						
					</g:form>
					
			</lx:ibox>
			
			
		</div>
	</div>
	<script type="text/javascript">

		$(function(){
			 $('.chosen-select').chosen();
             $(".numeric").autoNumeric('init',{vMin:'0'},{vMax:'9999'});
		});
	</script>
	

</body>
</html>
