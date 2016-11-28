<html>
<head>
	<meta charset="UTF-8">
	<title>Calendario ${calendarioInstance.id}</title>
</head>
<body>

	<div class="wrapper wrapper-content   animated fadeInRight">
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<lx:ibox>
					<lx:iboxTitle title="Calendario ${calendarioInstance.ejercicio} (${calendarioInstance.tipo})"/>
					<lx:errorsHeader bean="${calendarioInstance}"/>
					
					<g:form action="update" role="form" class="form-horizontal" >
						<g:hiddenField name="id" value="${calendarioInstance.id}"/>
                        <g:hiddenField name="version" value="${calendarioInstance.version}"/>
                        <lx:iboxContent>
                        	<lx:warningLabel />
                        	<f:with bean="calendarioInstance">
                        		<f:display property="ejercicio" />
                        		<f:display property="tipo" />
                        		<f:field property="comentario" widget-class="form-control" />
                        	</f:with>
                        </lx:iboxContent>
                        <lx:iboxFooter>
                        	<lx:backButton class="btn btn-default btn-outline btn-sm"/>
                        	<button type="submit" class="btn btn-primary btn-outline btn-sm">
		      					<i class="fa fa-floppy-save"></i> Salvar
		      				</button>
                        	
                        </lx:iboxFooter>
					</g:form>
				</lx:ibox>
			</div>
		</div>

		<div class="row">
			<div class="col-md-12">
				<lx:ibox>
					<lx:iboxTitle title="Periodos"/>
					<lx:iboxContent>
						<g:render template="periodosGrid"/>
					</lx:iboxContent>
					<lx:iboxFooter>
						
						<g:link action="createPeriodo" class="btn btn-outline btn-info btn-sm" id="${calendarioInstance.id}">
							<i class="fa fa-plus"></i> Agregar
						</g:link>
					</lx:iboxFooter>
				</lx:ibox>
			</div>
		</div>
	<div>
	
	
</body>
</html>