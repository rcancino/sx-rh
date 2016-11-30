<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Asistencia</title>
	<asset:javascript src="forms/forms.js"/>
</head>
<body>
	
	<lx:header>
		<h2>
			Asistencia: ${asistenciaInstance.empleado.perfil.numeroDeTrabajador} ${asistenciaInstance.empleado} <small>(${asistenciaInstance.empleado.perfil.puesto.clave})</small>
			
		</h2>
		<div class="col-md-4">
			<li><strong> ${asistenciaInstance.tipo} ${asistenciaInstance?.calendarioDet?.folio}</strong></li>
			<li> Turno: ${asistenciaInstance.empleado.perfil.turno.descripcion}</li>

		</div>
		<div class="col-md-4">
			<li>Retardo:    <strong> ${asistenciaInstance.retardoMayor}</strong></li>
			<li>Retardo comida:  <strong> ${asistenciaInstance.retardoComida}</strong></li>
			<li>Minutos NL:   <strong> ${asistenciaInstance.minutosNoLaborados} </strong></li>
		</div>

		<div class="col-md-4">
			<li>Faltas:    <strong> ${asistenciaInstance.faltas}</strong></li>
			<li>Asistencias:  <strong> ${asistenciaInstance.asistencias}</strong></li>
			<li>Vacaciones:   <strong> ${asistenciaInstance.vacaciones} </strong></li>
			<li>Incapacidad:   <strong> ${asistenciaInstance.incapacidades} </strong></li>
		</div>

		<lx:warningLabel/>
	</lx:header>

	<div class="row wrapper white-bg animated fadeInRight">
	
		<div class="">
			<div class="col-md-12">
				<div class="toolbar">
					<div class="btn-group">
						<g:link class="btn btn-default btn-outline " 
							data-toggle="tooltip" title="Anterior empleado"
							action="previous" id="${asistenciaInstance.id}"> 
							<i class="fa fa-arrow-left"></i> 
						</g:link>
						<g:link class="btn btn-default btn-outline" 
							data-toggle="tooltip" title="Siguiente empleado"
							action="next" id="${asistenciaInstance.id}"> 
							<i class="fa fa-arrow-right"></i> 
						</g:link>
						<g:link class="btn btn-default btn-outline" action="index" id="${asistenciaInstance.id}" 
							params="[calendario:asistenciaInstance?.calendarioDet?.id]"> 
							<span class="glyphicon glyphicon-list"></span> 
						</g:link>
						
						<g:link class="btn btn-default btn-outline" action="nomina" id="${asistenciaInstance.id}"> 
							<span class="glyphicon glyphicon-usd"></span>  
						</g:link>
						
						<a class="btn btn-default btn-outline" data-toggle="modal" data-target="#cambiarEmpleadoDialog"> 
							<span class="glyphicon glyphicon-search"></span> 
						</a>
					</div>

					<div class="btn-group">
						<button type="button" name="reportes"
							class="btn btn-success	 btn-outline dropdown-toggle" data-toggle="dropdown"
							role="menu">
							Reportes <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><g:jasperReport jasper="TarjetaDeAsistencia"
									format="PDF" name="Tarjeta">
									<g:hiddenField name="ID" value="${asistenciaInstance.empleado.id}" />
									<g:hiddenField name="CALENDARIO_ID" 
										value="${asistenciaInstance.calendarioDet.id}" />
									
								</g:jasperReport>
							</li>
							<li><g:jasperReport jasper="RetardoMensualPorEmpleado"
									format="PDF" name="Retardo mensual">
									<g:hiddenField name="ID" 
										value="${asistenciaInstance.empleado.id}" />
									
								</g:jasperReport>
							</li>
							<li>
							
								<a  data-toggle="modal" data-target="#calendarioForm"> Mensual X Empleado</a>
							</li>
							
							<li>
								<a data-toggle="modal" data-target="#periodoCalendarioForm"> Tarjeta X Periodo</a>
							</li>
						</ul>
					</div>
					
					<!-- Fin .btn-group -->

					<div class="btn-group">
						<button type="button" name="reportes"
							class="btn btn-primary btn-outline dropdown-toggle" data-toggle="dropdown">
							Operaciones <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><g:link 
									action="actualizar" id="${asistenciaInstance.id}">
									<span class="glyphicon glyphicon-cog"></span> Actualizar
						</g:link></li>
							<li><g:link action="delete" id="${asistenciaInstance.id}"
									onclick="return confirm('Eliminar toda la asistencia?');">
									<span class="glyphicon glyphicon-trash"></span> Eliminar
						</g:link></li>
						</ul>
					</div>

					<div class="btn-group">
						<a class="btn btn-default btn-outline"><i class="fa fa-pencil"></i></a>
					</div>
					<div class="btn-group">
						<a class="btn btn-default btn-outline" disabled> <strong>Dias: ${asistenciaInstance.diasTrabajados}</strong></a>
						<a class="btn btn-default btn-outline" disabled> <strong>Faltas manuales: 0</strong></a>
						<a class="btn btn-default btn-outline" disabled> <strong>Min a descuntar:: 0</strong></a>
					</div>
					

				</div>
			</div>

			<div class="col-md-12 toolbar">
				<div class="btn-group">
					
					<g:form class="form-inline" role="form" 
						controller="asistencia" 
						action="actualizar" id="${asistenciaInstance.id}">
						
						<label class="" for="diasTrabajadosField">Dias </label>
							<div class="form-group">
							<input type="text"
								name="diasTrabajados" 
								class="form-control" 
								id="diasTrabajadosField" 
								value="${asistenciaInstance.diasTrabajados}">
							</div>
							
							<label class="" for="faltasManualesField">Faltas manuales </label>
							<div class="form-group">
							<input type="text"
								name="faltasManuales" 
								class="form-control" 
								id="faltasManualesField" 
								value="${asistenciaInstance.faltasManuales}">
							</div>
							<div class="form-group">
								<label class="" for="diasTrabajadosField">Min a desc </label>
							<input type="text"
								name="minutosPorDescontar" 
								class="form-control " 
								id="minutosPorDescontarField" 
								value="${asistenciaInstance.minutosPorDescontar}">
							<button type="submit" class="btn btn-default">
								<span class="glyphicon glyphicon-ok"></span> 
							</button>
							</div>
							
						</g:form>
				</div>
			</div>
		</div> 
				
		<div class="row">
			<div class="col-md-12">
				<div class="grid-panel">
					<g:render template="asistenciaDetGridPanel" />
				</div>
			</div>
		</div>
				

		<g:render template="cambiarEmpleadoDialog"/>
	</div>
		
	
	
	


	
<g:render template="calendarioPeriodoDialog" model="[periodos:periodos]"/>
<g:render template="periodoDialog" model="[periodos:periodos]"/>
<script type="text/javascript">
	$(function(){
		$(".decimalField").autoNumeric('init',{vMin:'0'},{vMax:'9999'});
	});
</script>	

</body>
</html>
