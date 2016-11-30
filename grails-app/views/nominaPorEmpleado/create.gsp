<%@ page import="com.luxsoft.sw4.rh.NominaPorEmpleado" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Nomina</title>
</head>
<body>
	<lx:header>
		<g:link controller="nomina" action="show" id="${command.nomina.id}">
			<h2>Nómina: ${command.nomina.folio} ${command.nomina.periodicidad} </h2>
		</g:link>
	</lx:header>

	<div class=" row wrapper wrapper-content  animated fadeInRight">
		<div class="col-md-6 col-md-offset-3">
			<lx:ibox>
				<lx:iboxTitle title="Propiedades"/>
					<lx:iboxContent>
						<g:form action="agregar" class="form-horizontal"  >
							<g:hiddenField name="nomina.id" value="${command.nomina.id}"/>
							<formset>
								<f:with bean="command">
									<f:field property="empleado"/>
								</f:with>
							</formset>
							<div class="form-group">
				    			<div class=" col-sm-10">
				    				<lx:backButton controller="nomina" action="show" id="${command.nomina.id}"></lx:backButton>
				      				<button type="submit" class="btn btn-primary">
				      					<span class="glyphicon glyphicon-floppy-save"></span> Agregar
				      				</button>
				      				<g:link action="index"  class="btn btn-default"> Cancelar</g:link>

				    			</div>
							</div>
						</g:form>
					</lx:iboxContent>
			</lx:ibox>
			
			
		</div>
	</div>
	
	

	

</body>
</html>
