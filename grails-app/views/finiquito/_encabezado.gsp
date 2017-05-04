<lx:ibox>
    <lx:iboxTitle title="Finiquito:  ">
            ${entity.empleado} (${empleado.perfil.puesto.clave}) 
        <small>/${empleado.perfil.ubicacion} / ${empleado.salario.periodicidad}</small>
    </lx:iboxTitle>
    <lx:iboxContent>
        <div class="row alert alert-info">
            <div class="col-md-3 ">
                <ul>
                    <li>SDI: ${entity.salarioDiarioIntegrado}</li>
                    <li>Salario: ${entity.salario}</li>
                    <li>Sueldo promedio: ${entity.salario * 30}</li>
                    <li>Días por pagar: ${entity.diasPorPagar}</li>
                </ul>
            </div>
            <div class="col-md-3">
                <ul>
                    <li>Días del ejercicio: ${entity.diasDelEjercicio}</li>
                    <li>Días Trabajados: ${entity.diasTrabajadoEjercicio}</li>
                    <li>Días Trabajados para vac: ${entity.diasTrabajadoParaVacaciones}</li>
                    <li>Días Aguinaldo: ${entity.diasAguinaldo}</li>
                </ul>
            </div>
            <div class="col-md-3">
                <ul>
                    <li>Vacaciones anteriores: ${entity.vacacionesAnteriores}</li>
                    <li>Vacaciones ejercicio: ${entity.vacacionesEjercicio}</li>
                    <li>Vacaciones aplicadas: ${entity.vacacionesAplicadas}</li>                    
                    <li>Vacaciones finiquito:
                        <g:formatNumber number="${((entity.diasTrabajadoParaVacaciones * entity.vacacionesEjercicio / entity.diasDelEjercicio) + entity.vacacionesAnteriores - entity.vacacionesAplicadas)}" type="number"
                            maxFractionDigits="3" roundingMode="HALF_EVEN" />
                    </li>
                </ul>
            </div>
            <div class="col-md-3">
                <ul>
                    <li>Ingreso: ${entity.alta.text()}</li>
                    <li>Baja: ${entity.baja.fecha.text()}</li>
                    <li>Antigüedad (Días): ${entity.antiguedad}</li>
                    <li>Antigüedad (Años): ${entity.anosTrabajados }</li>
                </ul>
            </div>
        </div>
    </lx:iboxContent>
</lx:ibox>