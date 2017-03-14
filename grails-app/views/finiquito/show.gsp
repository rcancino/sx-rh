
<!doctype html>
<html>
	<head>
		<meta name="layout" content="application">
		<g:set var="entity" value="${finiquitoInstance}" scope="request"/>
		<g:set var="entityName" value="Finiquito" scope="request"/>
		<g:set var="editable" value="true" scope="request"/>
		<g:set var="delete" value="true" scope="request"/>
		<title>Finiquito: ${entity.empleado}</title>
	</head>
	<body>
        <g:set var="empleado" value="${finiquitoInstance.empleado}" scope="request"/>
		<lx:wrapper>
            <g:render template="encabezado" bean="${empleado}"></g:render>
            

			<div class="col-md-6">
				<lx:ibox>
					<lx:warningLabel/>

				<g:form name="updateForm" action="update" class="form-horizontal" method="PUT">

                  <lx:header>
                        <div class="btn-group">
                            <lx:backButton/>
                            <g:link class="btn btn-success btn-outline" controller="finiquitoDet" action="create" id="${finiquitoInstance.id}">
                                <i class="fa fa-plus"></i> Agregar 
                            </g:link>
                            <g:if test="${editable}">
                                <button id="saveBtn" class="btn btn-primary ">
                                    <i class="fa fa-floppy-o"></i> Actualizar
                                </button>
                            </g:if>
                            <g:if test="${printable}">
                                <lx:printButton id="${entity?.id}"/>
                            </g:if>

                            <g:if test="${delete}">
                                <a href="" class="btn btn-danger " 
                                    data-toggle="modal" 
                                    data-target="#deleteDialog">
                                    <i class="fa fa-trash">
                                    </i> Eliminar
                                </a> 

                            </g:if>
                            <div class="btn-group">
                                    <button type="button" name="reportes"
                                        class="btn btn-default btn-outline dropdown-toggle" data-toggle="dropdown"
                                            role="menu">
                                                Reportes <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <g:link action="reporte" params="[tipo:'1',finiquito:finiquitoInstance.id]"> Carta de Renuncia</g:link>
                                            <g:link action="reporte" params="[tipo:'2',finiquito:finiquitoInstance.id]">Recibo Separacion Calculo</g:link>
                                           <g:link action="reporte" params="[tipo:'3',finiquito:finiquitoInstance.id]">Recibo Separacion Nomina Calc</g:link>
                                            <g:link action="reporte" params="[tipo:'4',finiquito:finiquitoInstance.id]">Recibo Separacion Empleado</g:link>    
                                        </li>
                                        
                                    </ul>
                            </div>
                                    
                        </div>
                    </lx:header>
					<g:hiddenField name="id" value="${entity.id}"/>
                    <g:hiddenField name="version" value="${entity.version}"/> 
                    <div class="ibox-content">
                        <lx:errorsHeader bean="${entity}"/>
                        <f:with bean="${entity}">
							<f:field property="diasPorPagar" widget-class="form-control" wrapper="bootstrap3"/>
                            <f:field property="montoIntereses" widget-class="form-control" wrapper="bootstrap3"/>
                            <f:field property="tasaInteres" widget-class="form-control" wrapper="bootstrap3"/>
                            <f:field property="liq" widget-class="form-control" wrapper="bootstrap3"/> 
                            <%-- Temporalmente deshabilitado hasta que el usuario solicite la activacion del calculo de finiquito usando estas opciones  --%>
                       <%--     <f:field property="sdiOpcion" widget-class="form-control" wrapper="bootstrap3"/>
                            --%>
						</f:with>
                    </div>
                </g:form>	
                </lx:ibox>
                <g:render template="partidasPanel"/>
			</div>

			<div class="col-md-6">
				<g:render template="propiedadesPanel"/>
			</div>

            <div class="col-md-8 col-md-offset-2" >
                
            </div>
		</lx:wrapper>
	
		<g:render template="deleteFiniquitoDialog"></g:render>
	
	</div>	
		<script>
			$(function(){
				var res = $('.grid').dataTable({
            		responsive: true,
            		aLengthMenu: [[20, 40, 60, 100, -1], [20, 40,60, 100, "Todos"]],
            		"language": {
                	"url": "${assetPath(src: 'datatables/dataTables.spanish.txt')}"
            		},
            		"dom": 'ft<"clear">lrip',
            		"tableTools": {
                	"sSwfPath": "${assetPath(src: 'plugins/dataTables/swf/copy_csv_xls_pdf.swf')}"
            		},
            		"order": []
        		});
			});
		</script>
	</body>
</html>
