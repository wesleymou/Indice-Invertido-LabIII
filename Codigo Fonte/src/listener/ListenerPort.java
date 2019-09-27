package listener;

import java.awt.Desktop;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

public class ListenerPort implements Container {
	public void handle(Request request, Response response) {
		try {
			String path = request.getPath().getPath();
			//			String method = request.getMethod();
			System.out.println("Request: " + request.getQuery().toString());

			if (path.startsWith("/cadastrarEvento")/* && "POST".equals(method)*/) {
				System.out.println("");
				this.enviaResposta(Status.CREATED, response, "");

			} else {
				this.naoEncontrado(response, path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void naoEncontrado(Response response, String path) throws Exception {
		enviaResposta(Status.OK, response, "Não encontrado.");
	}

	private void enviaResposta(Status status, Response response, String string) throws Exception {
		PrintStream body = response.getPrintStream();
		long time = System.currentTimeMillis();
		response.setValue("Access-Control-Allow-Origin", "*");
		response.setValue("Content-Type", "application/json");
		response.setValue("Server", "");
		response.setDate("Date", time);
		response.setDate("Last-Modified", time);
		response.setStatus(status);
		body.println(string);
		body.close();
	}

	public static void main(String[] list) throws Exception {

		int porta = 1880;

		// Configura uma conexão soquete para o servidor HTTP.
		Container container = new ListenerPort();
		ContainerSocketProcessor servidor = new ContainerSocketProcessor(container);
		Connection conexao = new SocketConnection(servidor);
		SocketAddress endereco = new InetSocketAddress(porta);
		conexao.connect(endereco);

//		Desktop.getDesktop().browse(new URI("https://pucweb-wesley-mouraria.azurewebsites.net/"));

		System.out.println("Interromper o servidor? (y/n)");

		Scanner ler = new Scanner(System.in);
		String a = "";
		while (!a.equals("y")) {
			a = ler.next();
			if (a.equals("n"))
				System.out.println("Ent�o n�o.");
			else if (!a.equals("y") && !a.equals("n"))
				System.out.println("Sem tempo irm�o.");
		}
		ler.close();
		conexao.close();
		servidor.stop();
		System.out.println("Servidor terminado.");
	}
}
