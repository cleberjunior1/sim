package io.sim;

import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;

import it.polito.appeal.traci.SumoTraciConnection;

class ObjInicializacao implements Iterable<ObjInicializacao> {
	private Car carro;
	private Route rota;

	public ObjInicializacao(Car carro, Route rota) {
		this.carro = carro;
		this.rota = rota;
	}

	@Override
	public Iterator<ObjInicializacao> iterator() {
		return new Iterator<ObjInicializacao>() {
			private boolean hasNext = true;

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public ObjInicializacao next() {
				hasNext = false; // Indica que não há mais elementos
				return ObjInicializacao.this;
			}
		};
	}

	public Car getCarro() {
		return this.carro;
	}

	public Route getRota() {
		return this.rota;
	}

	// ... outros membros da classe ...
}

public class EnvSimulator extends Thread {

	private SumoTraciConnection sumo;
	private ArrayList<Driver> drivers = new ArrayList<Driver>();
	private ArrayList<ObjInicializacao> listaDeMapas = new ArrayList<ObjInicializacao>();

	public EnvSimulator() {

	}

	public void run() {

		/* SUMO */
		String sumo_bin = "sumo-gui";
		String config_file = "map/map.sumo.cfg";

		// Sumo connection
		this.sumo = new SumoTraciConnection(sumo_bin, config_file);
		sumo.addOption("start", "1"); // auto-run on GUI show
		sumo.addOption("quit-on-end", "1"); // auto-close on end

		try {
			sumo.runServer(12345);
			initializeDrivers();
			while (isAlive()) {
				for (ObjInicializacao objeto : listaDeMapas) {
					initializeRoute(objeto);
				}
				Thread.sleep(5000);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeDrivers() {
		for (Driver driver : drivers) {
			driver.getCarro().getAuto().start();
			System.out.println(driver.getId());
		}
	}

	public SumoTraciConnection getSumoObj() {
		return this.sumo;
	}

	private void initializeRoute(ObjInicializacao objeto) {
		if (objeto.getRota().getItinerario().isOn()) {
			TransportService tS1 = new TransportService(true, "CAR1", objeto.getRota().getItinerario(),
					objeto.getCarro().getAuto(), sumo);
			tS1.start();
		}
	}

	public void newRoute(Car carro, Route rota) {
		ObjInicializacao objeto = new ObjInicializacao(carro, rota);
		listaDeMapas.add(objeto);
	}

	public void setDrivers(ArrayList<Driver> motoristas) {
		this.drivers = motoristas;
	}

}