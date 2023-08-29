import java.sql.*;
import java.util.Scanner;

public class registro {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*****BIENVENIDOS*****");

        System.out.println("¿Deseas registrarte o iniciar sesion?:");
        String respuesta = scanner.nextLine();

        if (respuesta.equals("registrar")) {

        System.out.print("Ingrese su usuario: ");
        String usuario = scanner.nextLine();

        System.out.print("Ingrese su contrase\u00f1a: ");
        String pass = scanner.nextLine();

        System.out.println("Ingrese tipo de usuario: ");
        String tipo = scanner.nextLine();

            if (usuario.equals("") || pass.equals("") || tipo.equals("")){
                System.out.println("No se admiten datos vacios.");
            }else if (pass.length()<=8){
                System.out.println("La contraseña debe contener mas de 8 caracteres");
            }else {

                String driver = "com.mysql.cj.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/registro_usuario";
                String username = "root";
                String password = "";

                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM usuarios");//TABLA

                    Insert(usuario, pass, tipo, connection);
                    connection.close();
                    statement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }else {
            if (respuesta.equals("iniciar sesion")) {
                System.out.println("Ingrese el email o usuario: ");
                String email = scanner.nextLine();

                System.out.println("Ingrese su contrase\u00f1a: ");
                String contra = scanner.nextLine();

                String driver = "com.mysql.cj.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/registro_usuario";
                String username = "root";
                String password = "";

                try {
                Class.forName(driver);
                Connection connection = DriverManager.getConnection(url, username, password);

                String consultaSQL = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ?";

                    PreparedStatement statement = connection.prepareStatement(consultaSQL);
                    statement.setString(1, email);
                    statement.setString(2, contra);


                    // Ejecutar la consulta
                    ResultSet resultSet = statement.executeQuery();

                    // Procesar el resultado si existe
                    if (resultSet.next()) {
                        String usuario = resultSet.getString("usuario");
                        String pass = resultSet.getString("contraseña");
                        String tipo = resultSet.getString("tipo");

                        if (tipo.equals("administrativo")){
                            System.out.println("sesion iniciada " + tipo);
                            System.out.println("Deseas eliminar un usuario?: ");
                            String eliminar = scanner.nextLine();

                            if (eliminar.equals("si")){
                                System.out.println("Que usuario deseas eliminar? ");
                                String emailborrar = scanner.nextLine();
                                String sentenciaSQL = "DELETE FROM usuarios WHERE usuario = ?";
                                PreparedStatement prepareStatement = connection.prepareStatement(sentenciaSQL);
                                prepareStatement.setString(1, emailborrar);
                                prepareStatement.executeUpdate();

                                System.out.println("usuario eliminado correctamente");
                            }

                        }else if
                            (tipo.equals("estandar")){
                            System.out.println("Bienvenido usuario : " + usuario);
                            }

                    } else {
                        System.out.println("No se encontró un usurio registrado con estas credenciales.");
                    }

                    // Cerrar recursos
                    resultSet.close();
                    statement.close();
                    connection.close();

                }catch (SQLException e){
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    public static void Insert(String usuario, String pass, String tipo, Connection connection){

        try {
            // Sentencia INSERT
            String sql = "INSERT INTO usuarios (usuario, contraseña, tipo) VALUES (?, ?, ?)";

            // Preparar la sentencia
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, pass);
            preparedStatement.setString(3, tipo);


            // Ejecutar la sentencia
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("usuario " + usuario + " Registrado exitosamente.");
            } else {
                System.out.println("No se pudo insertar el registro.");
            }

            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}