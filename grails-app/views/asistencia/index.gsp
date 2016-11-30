<%@ page import="com.luxsoft.sw4.rh.Asistencia" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Asistencias</title>
</head>
<body>

<div class="row wrapper border-bottom white-bg page-heading">
    <div class="col-lg-10">
    	<h2>Control de asistencia (${session.ejercicio})</h2>
    	<a href="" data-toggle="modal" data-target="#calendarioDeAsistenciaForm">
			Lista de asistencia ${tipo} ${calendarioDet?.folio} (${calendarioDet?.asistencia})
		</a>
        <g:if test="${flash.message}">
            <small><span class="label label-warning ">${flash.message}</span></small>
        </g:if> 
        <g:if test="${flash.error}">
            <small><span class="label label-danger ">${flash.error}</span></small>
        </g:if> 
    </div>
</div>

<div class="row wrapper wrapper-content white-bg animated fadeInRight">
	<div class="row button-panel">

			<div class="col-md-4 toolbar">
				<input id="searchField" class="form-control" type="text" placeholder="Empleado" autofocus="autofocus">
			</div>

			<div class="col-md-6">
			
				<div class="btn-group ">

					<g:link action="index" 
						class="btn btn-outline ${tipo=='SEMANA'?'btn-primary':'btn-default'}" 
						params="[tipo:'SEMANA']"> Semana
					</g:link>
					
					<g:link action="index" 
						class="btn  btn-outline ${tipo=='QUINCENA'?'btn-primary':'btn-default'}" 
						params="[tipo:'QUINCENA']"> Quincena
					</g:link>
					
				</div>
				
				<div class="btn-group">
					<button type="button" name="reportes"
						class="btn btn-outline btn-default dropdown-toggle" data-toggle="dropdown"
						role="menu">
						Operaciones <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							<g:if test="${calendarioDet}">
								<g:link action="actualizarAsistencias" class="" 
									onclick="return confirm('Actualizar la asistencia de todos los empleados?','${tipo} ${calendarioDet?.folio}');" 
									id="${calendarioDet.id}">
									<span class="glyphicon glyphicon-cog"></span> Actualizar
								</g:link>
							</g:if>
						</li>
						<li>
							<g:link action="eliminarAsistencias" class="" 
								onclick="return confirm('Eliminar las asistencias de todos los empleados? (${tipo} ${calendarioDet?.folio})');" 
								id="${calendarioDet.id}"
								params="[tipo:tipo]">
								<span class="glyphicon glyphicon-trash"></span> Eliminar
							</g:link>
						</li>
						<li>
							<g:link action="depurar" class="" 
								onclick="return confirm('Depurar ? (${tipo} ${calendarioDet?.folio})');" 
								id="${calendarioDet.id}"
								params="[tipo:tipo]">
								 Depurar
							</g:link>
						</li>
					</ul>
					
				</div>
			
				<div class="btn-group">
					<button type="button" name="reportes"
						class="btn btn-outline btn-default dropdown-toggle" data-toggle="dropdown"
						role="menu">
						Reportes <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li>
							
							<g:jasperReport jasper="Ausentismo"
									format="PDF" name="Ausentismo">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
								
								
							</g:jasperReport>
							
							<g:jasperReport jasper="RetardoComida"
									format="PDF" name="Ret Comida">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
								
								
							</g:jasperReport>
							
							<g:jasperReport jasper="RetardoMenor"
									format="PDF" name="Ret Menor">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
							<g:jasperReport jasper="RetardoMayor"
									format="PDF" name="Ret Mayor">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
							<g:jasperReport jasper="ChecadasFaltantes"
									format="PDF" name="Cheq Falt.">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
							<g:jasperReport jasper="TarjetaDeAsistencia"
								format="PDF" name="Tarjetas">
								<g:hiddenField name="ID" value="%" />
								<g:hiddenField name="CALENDARIO_ID" 
									value="${calendarioDet.id}" />
							</g:jasperReport>
							
							<g:jasperReport jasper="MinutosNoLaborados"
								format="PDF" name="Mins. no Laborados">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
							<g:jasperReport jasper="VacacionesPorCalendario"
								format="PDF" name="Vacaciones">
								<g:hiddenField name="CALENDARIODET" 
										value="${calendarioDet.id}" />
							</g:jasperReport>
							
						</li>
						
						<li>
							<a class="" 
								data-toggle="modal" 
								data-target="#calendarioForm"> Mensual X Empleado</a>
						</li>
						
					</ul>
				</div>
			</div>
			
	</div>
  	
  	<div class="row">
  		<div class="col-md-12">
  				<ul class="nav nav-tabs" role="tablist">
  				  <li class="${tipo=='SEMANA'?'active':''}">
  				  	<a href="#andrade" role="tab" data-toggle="tab">Andrade</a>
  				  </li>
  				  <li><a href="#bolivar" role="tab" data-toggle="tab">Bolivar</a></li>
  				  <li><a href="#calle4" role="tab" data-toggle="tab">Calle 4</a></li>
  				  <li><a href="#cf5febrero" role="tab" data-toggle="tab">5 de Febrero</a></li>
  				  <li><a href="#tacuba" role="tab" data-toggle="tab">Tacuba</a></li>
  				  <li class="${tipo=='QUINCENA'?'active':''}">
  				  	<a href="#oficinas" role="tab" data-toggle="tab">Oficinas</a>
  				  </li>
  				  <li><a href="#ventas" role="tab" data-toggle="tab">Ventas</a></li>
  				</ul>

  				<div class="tab-content">
  			  		<div class="tab-pane ${tipo=='SEMANA'?'active':''}" id="andrade">
  						<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap.ANDRADE]"/>
  			  		</div>
  			  		<div class="tab-pane" id="bolivar">
  			  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['BOLIVAR']]"/>
  			  		</div>
  			  		<div class="tab-pane" id="calle4">
  			  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['CALLE4']]"/>
  			  		</div>
  			  		<div class="tab-pane" id="cf5febrero">
  			  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['CF5FEBRERO']]"/>
  			  		</div>
  			  		<div class="tab-pane" id="tacuba">
  			  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['TACUBA']]"/>
  			  		</div>
  			  		<div class="tab-pane ${tipo=='QUINCENA'?'active':''}" id="oficinas">
  			  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['OFICINAS']]"/>
  			  		</div>
  			  		<div class="tab-pane" id="ventas">
  			  			<g:render template="asistenciaGridPanel" model="['partidasList':partidasMap['VENTAS']]"/>
  			  		</div>
  				</div>
  		</div>
		
		<g:render template="/_common/selectorDeAsistencia"/>
	</div>

  	<g:render template="calendarioPeriodoDialog" model="[periodos:periodos]"/>
  	<g:render template="/_common/cambioDeEjercicioDialog"/>
</div>

<script type="text/javascript">
	$(function(){
		$('.grid').dataTable({
            responsive: true,
            aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
            "language": {
                "url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
            },
            "dom": 't<"clear">lfrip',
            "tableTools": {
                "sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
            },
            "order": []
        });
        var tables = $('.grid'); //.DataTable();
        $("#searchField").on('keyup',function(e){
            var term=$(this).val();
            $.each(tables, function(p,v) {
                $(this).DataTable().search(term).draw();
            });
        });
	});
</script>	
	
</body>
</html>