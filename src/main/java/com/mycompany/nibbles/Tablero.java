/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//modificado por camilo josue bendeck machado
//abajo de esto esta el programa sin editar
package com.mycompany.nibbles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import javax.swing.JButton;
//codigo agregado1

//codigo finalizado 1
/**
 *
 * @author Carlos
 */
public class Tablero extends JPanel implements ActionListener{

    private final int ANCHO_TABLERO = 600;
    private final int ALTURA_TABLERO = 600;
    private final int TAM_PUNTO = 10;
    private final int RAND_POS = 59;
    private final int RETRASO = 140;
    private final int TODOS_PUNTOS = 3600;
    
    private final int x[] = new int[TODOS_PUNTOS];
    private final int y[] = new int[TODOS_PUNTOS];
    
    private Image punto;
    private Image cabeza;
    private Image manzana;
    
    private Timer timer;
    private int puntos;
    private int manzana_x;
    private int manzana_y;
    
    private boolean enJuego = true;
    private boolean dirDerecha = true;
    private boolean dirArriba = false;
    private boolean dirIzquierda = false;
    private boolean dirAbajo = false;
    private void reiniciarJuego() {
    enJuego = true;
    puntaje = 0;
    inicializarJuego();
}
    public Tablero(){
        inicializarTablero();
    }
    int puntaje;
    public void inicializarTablero(){
        addKeyListener(new AdaptadorTeclado());
        setBackground(Color.black);
        setFocusable(true);
        
        setPreferredSize(new Dimension(ANCHO_TABLERO, ALTURA_TABLERO));
        cargarImagenes();
        inicializarJuego();
    }
    
    private void cargarImagenes(){
        ImageIcon iiPunto = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/dot.png");
        punto = iiPunto.getImage();
        
        ImageIcon iiCabeza = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/head.png");
        cabeza = iiCabeza.getImage();
        
        ImageIcon iiManzana = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/apple.png");
        manzana = iiManzana.getImage();
    }
    
    private void inicializarJuego(){
        puntos = 3;
        for(int i=0; i<puntos; i++){
            x[i] = 50 -i * 10;
            y[i] = 50;
        }
        
        posicionarManzana();
        
        timer = new Timer(RETRASO, this);
        timer.start();
    }
    
    private void posicionarManzana(){
        int r = (int)(Math.random() * RAND_POS);
        manzana_x = ((r * TAM_PUNTO));
        
        r = (int)(Math.random() * RAND_POS);
        manzana_y = ((r * TAM_PUNTO));
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        pintar(g);
    }
    
    private void pintar(Graphics g){
        if(enJuego){
            g.drawImage(manzana, manzana_x, manzana_y, this);
            
            for(int i =0; i< puntos; i++){
                if(i == 0){
                    g.drawImage(cabeza, x[i], y[i], this);
                }else{
                    g.drawImage(punto, x[i], y[i], this);
                }
            }
            
            Toolkit.getDefaultToolkit().sync();
          //CODIGO NUEVO
          g.setColor(Color.red);
          g.setFont(new Font("Ink Free",Font.BOLD, 20));
          FontMetrics metrics = getFontMetrics(g.getFont());
          g.drawString("PUNTAJE: " + puntaje, (ANCHO_TABLERO - metrics.stringWidth("PUNTAJE: "+ puntaje)/1),ALTURA_TABLERO/8);
          
        }else{
            finJuego(g);
        }
    }
    
    private void finJuego(Graphics g){
        String msj = "Fin de Juego";
        String msjscore = "SU PUNTAJE FINAL ES: " + puntaje;
        String msjreinicio = "PARA VOLVER A JUGAR PRECIONE LA TECLA: R";
        Font peq = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(peq);
        
        g.setColor(Color.white);
        g.setFont(peq);
        g.drawString(msj, (ANCHO_TABLERO - metrics.stringWidth(msj))/2, ALTURA_TABLERO/2);
        g.drawString(msjscore, (ANCHO_TABLERO - metrics.stringWidth(msj))/2, ALTURA_TABLERO/3);
        g.drawString(msjreinicio, (ANCHO_TABLERO - metrics.stringWidth(msj))/2, ALTURA_TABLERO/1);
        //editando inicio juego
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(enJuego){
            verificarManzana();
            verificarColision();
            Mover();
        }
        
        repaint();
    }
    
    private void verificarManzana(){
        if((x[0] == manzana_x) && (y[0] == manzana_y)){
            puntos++;
            posicionarManzana();
            puntaje = puntaje + 1;
        }
    }
    
    private void verificarColision(){
        for(int i = puntos; i>0; i--){
            
            //Cabeza colisiono con cola
            if((i > 4) && (x[0] == x[i]) && (y[0] == y[i])){
                enJuego = false;
            }
            
            if(y[0] >= ALTURA_TABLERO){
                enJuego = false;
            }
            
            if(y[0] < 0){
                enJuego = false;
            }
            
            if(x[0] >= ANCHO_TABLERO){
                enJuego = false;
            }
            
            if(x[0] < 0){
                enJuego = false;
            }
            
            if(!enJuego){
                timer.stop();
            }
        }
    }
    
    private void Mover(){
        
        //mover los puntos verdes siguiendo la ultima ubicacion del punto rojo
        for(int i = puntos; i > 0; i--) {
        x[i] = x[i-1];
        y[i] = y[i-1];
    }

    // Ajustar la posición de la cabeza según los bordes del tablero
    if (dirIzquierda && x[0] <= 0) {
        x[0] = ANCHO_TABLERO - TAM_PUNTO;
    } else if (dirDerecha && x[0] >= ANCHO_TABLERO - TAM_PUNTO) {
        x[0] = 0;
    } else if (dirArriba && y[0] <= 0) {
        y[0] = ALTURA_TABLERO - TAM_PUNTO;
    } else if (dirAbajo && y[0] >= ALTURA_TABLERO - TAM_PUNTO) {
        y[0] = 0;
    }

    // Movimiento normal de la cabeza
    if(dirIzquierda){
        x[0] -= TAM_PUNTO;
    }
    
    if(dirDerecha){
        x[0] += TAM_PUNTO;
    }
    
    if(dirArriba){
        y[0] -= TAM_PUNTO;
    }
    
    if(dirAbajo){
        y[0] += TAM_PUNTO;
    }
    }
    
    private class AdaptadorTeclado extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e){
            int tecla = e.getKeyCode();
            
            if((tecla == KeyEvent.VK_LEFT) && (!dirDerecha)){
                dirIzquierda = true;
                dirArriba = false;
                dirAbajo = false;
            }
            
            if((tecla == KeyEvent.VK_RIGHT) && (!dirIzquierda)){
                dirDerecha = true;
                dirArriba = false;
                dirAbajo = false;
            }
            
            if((tecla == KeyEvent.VK_UP) && (!dirAbajo)){
                dirArriba = true;
                dirDerecha = false;
                dirIzquierda = false;
            }
            
            if((tecla == KeyEvent.VK_DOWN) && (!dirArriba)){
                dirAbajo = true;
                dirDerecha = false;
                dirIzquierda = false;
            }
            if(tecla == KeyEvent.VK_R){
            reiniciarJuego();}
        }        
    }
}   
