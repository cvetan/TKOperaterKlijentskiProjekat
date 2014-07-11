/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package komunikacija;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;

/**
 *
 * @author cvetan
 */
public class Komunikacija {

    private Socket socket;
    private static Komunikacija instanca;

    private Komunikacija() throws RuntimeException{
        try {
            socket = new Socket("localhost", 9000);
            System.out.println("Uspostavljena veza sa serverom.");
        } catch (IOException e) {
            throw new RuntimeException("Nemate pristup serveru!");
        }
    }

    public static Komunikacija vratiInstancu() throws RuntimeException {
        if (instanca == null) {
            instanca = new Komunikacija();
        }
        return instanca;
    }

    public void posalji(TransferObjekatZahtev toZahtev) throws IOException {
        ObjectOutput outSocket = new ObjectOutputStream(socket.getOutputStream());
        outSocket.writeObject(toZahtev);
    }

    public TransferObjekatOdgovor procitaj() throws IOException, ClassNotFoundException {
        ObjectInputStream inSocket = new ObjectInputStream(socket.getInputStream());
        return (TransferObjekatOdgovor) inSocket.readObject();
    }

    public String obradiOdgovorServera(TransferObjekatOdgovor toOdgovor) throws RuntimeException {
        if (!toOdgovor.isUspesan()) {
            throw new RuntimeException(toOdgovor.getRezultat());
        } else {
            return toOdgovor.getRezultat();
        }
    }
}
