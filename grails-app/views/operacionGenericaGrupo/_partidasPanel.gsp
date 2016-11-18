<div class="ibox float-e-margins">
    <div class="ibox-title"> 
        <h5>Empleados</h5> 
        <div class="ibox-tools">
            <g:link action="selectorDeEmpleados" id="${operacionGenericaGrupoInstance.id}">
                    <i class="fa fa-plus"></i> 
            </g:link>
            <a class="collapse-link">
                <i class="fa fa-chevron-up"></i>
            </a>
            <a class="close-link">
                <i class="fa fa-times"></i>
            </a>
        </div>
    </div>
    <div class="ibox-content">
        <table id="grid" class="table table-striped table-bordered table-condensed">
            <thead>
                <tr>
                    <th>Folio</th>
                    <th>Empleado</th>
                    <th>Ubicación</th>
                    <th>E</th>
                </tr>
            </thead>
            <tbody>
                <g:each in="${operacionGenericaGrupoInstance.partidas}" var="row">
                    <tr >
                        <td>${g.formatNumber(number:row.id, format:'#####')}</td>
                        <td>${fieldValue(bean:row,field:"empleado.nombres")}</td>
                        <td>${fieldValue(bean:row,field:"empleado.perfil.ubicacion.clave")}</td>
                        <td><i class="fa fa-trash"></i></td>
                        
                    </tr>
                </g:each>
            </tbody>
        </table>
    </div>
</div>