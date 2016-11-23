<html>
<head>
	<meta charset="UTF-8">
	<title>INFONAVIT ${infonavitInstance.id }</title>
</head>
<body>

	<lx:header>
		<h2>Crédito INFONAVIT: ${infonavitInstance.empleado} (${infonavitInstance.empleado.clave } ID:${infonavitInstance.empleado.id })</h2>
		<lx:warningLabel/>
	</lx:header>

	<div class=" wrapper wrapper-content  animated fadeInRight">
		<lx:ibox>
			<lx:iboxTitle title="Propiedades"/>
			<lx:iboxContent>

				<div class="row">
					
					<form class="form-horizontal" >
						<f:with bean="${infonavitInstance}">
						<div class="col-md-6">
							<f:display property="numeroDeCredito" label="# de crédito" wrapper="bootstrap3"/>
							<f:display property="tipo" wrapper="bootstrap3"/>
							<f:display property="salarioMinimoGeneral" widget="numeric" label="SMG" wrapper="bootstrap3"/>
							<f:display property="cuotaDiaria" widget="numeric" wrapper="bootstrap3"/>
							<f:display property="bimestreActual" wrapper="bootstrap3"/>
							<f:display property="comentario" wrapper="bootstrap3"/>
						</div>
						<div class="col-md-6">
							<f:display property="alta" wrapper="bootstrap3"/>
							<f:display property="cuotaFija" label="Descuento" wrapper="bootstrap3"/>
							<f:display property="salarioDiarioIntegrado" widget="numeric" wrapper="bootstrap3" label="SDI"/>
							<f:display property="seguroDeVivienda" widget="numeric" wrapper="bootstrap3" label="Seguro"/>
							<label class="col-sm-3 control-label">Abonos acumulados</label>
							 <div class="col-sm-3">
							 	<p class="form-control-static">${abonos}</p>
							 </div>
						</div>
						</f:with>	
					</form>
					
				</div>
				
			</lx:iboxContent>
		</lx:ibox>

		<div class="row">
			<div class="col-md-12">
				<lx:ibox>
					<lx:iboxTitle title="Bimestres"/>
					<lx:iboxContent>
						<g:render template="bimestresGrid"/>
					</lx:iboxContent>
					<lx:iboxFooter>
						<lx:backButton/>
						<lx:createButton></lx:createButton>
						<lx:editButton id="${infonavitInstance.id}"/>
						<g:link action="editarBitacora" class="btn btn-success btn-outline" id="${infonavitInstance.id}"> Bitacora</g:link>
						<g:link action="calcularCuotaBimestral" id="${infonavitInstance.id}" 
							class="btn btn-outline btn-primary"
							onclick="return confirm('Calcular cuota para  bimestre actual?')"> 
							<i class="fa fa-cogs"></i> Re calcular
						</g:link>
						<a class="btn btn-danger btn-outline" 
                                            data-toggle="modal" data-target="#deleteDialog"><i class="fa fa-trash"></i> Eliminar</a> 
					</lx:iboxFooter>
				</lx:ibox>	
			</div>
		</div>

		<g:render template="/common/deleteDialog" bean="${infonavitInstance}"/>
	</div>


	
	
	
</body>
</html>