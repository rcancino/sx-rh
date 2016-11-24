<html>
<head>
	<meta charset="UTF-8">
	<title>Vacaciones grupo ${vacacionesGrupoInstance.id}</title>
	<asset:javascript src="forms/forms.js"/>
</head>
<body>

	<lx:header>
		<h2>${vacacionesGrupoInstance.comentario} <small>registro de vacaciones</small></h2>
		<lx:warningLabel bean="${vacacionesGrupoInstance}"/>
		<lx:errorsHeader bean="${vacacionesGrupoInstance}"/>
	</lx:header>

	<div class="row wrapper wrapper-content animated fadeInRight">
		
		<div class="col-md-6">
			<div class="ibox float-e-margins">
					<div class="ibox-title"> <h5>Propiedades</h5> </div>

					<div class="ibox-content">
						<g:form name="updateForm" action="update" class="form-horizontal" method="PUT">  
							<f:with bean="${vacacionesGrupoInstance}">
								<f:display property="comentario" widget-class="form-control" />
								<f:display property="fechaInicial"/>
								<f:display property="fechaFinal"/>
								<f:display  property="calendarioDet" widget-class="form-control"/>
							</f:with>
						</g:form>
					</div>

					<div class="ibox-footer">
						<div class="btn-group">
							<lx:backButton action="show" id="${vacacionesGrupoInstance.id}" />
						    <lx:editButton id="${vacacionesGrupoInstance.id}"/>
						    <lx:deleteButton bean="${vacacionesGrupoInstance}"/>
						</div>	
					</div>
			</div>
		</div>

		<div class="col-md-6">
			<g:render template="partidasPanel"></g:render>
		</div>
	</div>

	
	
</body>
</html>