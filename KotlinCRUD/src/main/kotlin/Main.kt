
// Reto de Concepto Create, Read, Update, Delete(CRUD) en Kotlin
/*
1-Crear crud productos -Id -Nombre -Codigo -Cantidad -precio

2-Validar precio mayor a 0 y cantidad mayor a 0.

3-Que no se repita el codigo del producto.

4-Script para generar base de datos.

5-Añadir filtro de busqueda en el listado.

6-Exportar registro a excel.
 */

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream



fun main() {
    println("### CRUD de una tienda ###\n")

    // var lista = mutableListOf<Producto>()

    // Validamos que podamos ingresar con el metodo
    Producto.addProducto(1, "Lona Grande PowerRaid", "LGPR-1", 10, 100.0)
    Producto.addProducto(2, "Lona Grande PowerRaid 2", "LGPR-2", 15, 400.0)
    Producto.addProducto(3, "Coca cola Light", "CCL", 33, 22.0)

    // Pruebo filtro, mando la palabra clave que quiero buscar y la lista de donde se buscara
    // utilizo la propia de la clase aunque podria ser una nueva de una nueva lista tipo Producto
    // tal que asi: var lista = mutableListOf<Producto>()
    println(Producto.filtroDeBusqueda("Coca", Producto.Companion.listaProductos))

    // Exportamos a excell
    exportToExcel(Producto.Companion.listaProductos)

}

// Punto 1 Crud
//
//
//  Vamos a definir nuestra clase productos 
data class Producto(val id: Int,val nombre: String,val codigo: String,val cantidad: Int,val precio: Double
){
    // Con el companion object puedo llamo mis metodos sin necesidad de instanciar otro objeto
    companion object {
        // Lista de productos, esta la ocupamos para agregar y validar codigo unico
        val listaProductos = mutableListOf<Producto>()

        fun addProducto(id: Int, nombre: String, codigo: String, cantidad: Int, precio: Double){
            // Creamos el producto
            val newProducto = Producto(id, nombre, codigo, cantidad, precio)

            // Validamos los datos del producto con el metodo validarPrecioYCantidad()
            newProducto.validarPrecioYCantidad()

            // Si pasa la validacion se añade el producto a la lista
            listaProductos.add(newProducto)
        }

        //Punto 5 Filtro
        //
        //
        // Filtro por cadena de texto
        fun filtroDeBusqueda(filtro: String, listaProductos: List<Producto>): List<Producto> {

            val listaFiltrada = mutableListOf<Producto>()
            for (producto in listaProductos) {
                if (producto.nombre.contains(filtro, ignoreCase = true) || producto.codigo.contains(filtro, ignoreCase = true)) {
                    // voy añadiendo  a la lista de retorno  todos los productos que cumplan
                    listaFiltrada.add(producto)
                }
            }
            return listaFiltrada

        }
    }



    // Punto 2 y 3, Validaciones
    //
    //
    fun validarPrecioYCantidad(){

        // Validamos cantidad mayor a 0
        if (cantidad <= 0){
            // Utilizo la excepcion predefinida 
            throw IllegalArgumentException("La cantidad de este producto es 0, debe ser mayor")
        }
        // Validamos precio mayor a 0
        if (precio  <= 0){
            throw IllegalArgumentException("El precio de este producto es 0, debe ser mayor")
        }
        // Validamos Codigo que no se repita
        // Any itera por todo el arreglo y es equiparable a un foreach o for (object in objects)
        // it.codigo hace mencion al objeto que itera y codigo al objeto que buscamos si existe
        if (listaProductos.any {it.codigo == codigo}){
            throw IllegalArgumentException("El codigo ya existe, no se puede repetir")
        }
    }

}

// Punto 6 Exportar a excel
//
//
fun exportToExcel(products: List<Producto>) {
    // Creamos el libro de trabajo de Excel
    val workbook = XSSFWorkbook()

    // Creamos la hoja de trabajo de Excel, podriamos poner una condicional para que
    // se omita si ya existe o se cree con otro nombre, digamos con la fecha actual
    val sheet = workbook.createSheet("Productos")

    // Crear una fila para las cabeceras de la tabla
    val headerRow = sheet.createRow(0)
    headerRow.createCell(0).setCellValue("ID")
    headerRow.createCell(1).setCellValue("Nombre")
    headerRow.createCell(2).setCellValue("Código")
    headerRow.createCell(3).setCellValue("Cantidad")
    headerRow.createCell(4).setCellValue("Precio")



    // Agregamos los datos de los productos a la tabla
    for ((index, product) in products.withIndex()) {
        val row = sheet.createRow(index + 1)
        // No deja trabajar con Int por ende se convierten a double
        row.createCell(0).setCellValue(product.id.toDouble())
        row.createCell(1).setCellValue(product.nombre)
        row.createCell(2).setCellValue(product.codigo)
        row.createCell(3).setCellValue(product.cantidad.toDouble())
        row.createCell(4).setCellValue(product.precio)
    }

    // Guardamos el libro de trabajo de Excel en un archivo
    val fileOut = FileOutputStream(File("productos.xlsx"))
    workbook.write(fileOut)
    fileOut.close()

    // Cerraramos el libro de trabajo de Excel
    workbook.close()
}



// Punto 4, Script a base de datos
// Script de base de datos (MySql Format):
//
//
// Create DATABASE IF NOT EXISTS Tienda;
// 
// CREATE TABLE Productos(
// id INT NOT NULL PRIMARY KEY,
// nombre VARCHAR(100) NOT NULL,
// codigo VARCHAR(100) NOT NULL UNIQUE,
// cantidad INT NOT NULL,
// precio DECIMAL(10,2) NOT NULL)
// 
// 
// 