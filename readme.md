# Simón No Dice

## Oliver Miguez Alonso

## Jorge Duran Cruz

### Explicación:

Se basa en el clásico juego del simón dice, principalmente centrandonos en el uso de corrutinas y el modelo MVVM,
completamente desarrollado en ***Kotlin*** y ***JetPack Compose*** de Android Studio.


### Funcionamiento



### Diagrama de estados:

```mermaid
    stateDiagram-v2
    [*] --> Inicio
    Inicio --> Generando:numeroRandom()
    Generando --> Adivinando:actualizarNumero()
    Adivinando --> Generando:correcionOpcionElegido()
    Adivinando --> Inicio:derrota()
```

