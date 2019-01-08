# UDP port knocking

Para aplikacji - serwer i klient realizujące autoryzację metodą "UDP port knocking".

## Działanie aplikacji

 1. < Server > Nasłuchiwanie na zadanych portach UDP na pakiety od wielu klientów.
 2. < Klient > Otwarcie portu UDP, z którego następuje wysłanie serii pakietów na zadane porty.
 3. < Klient > Oczekiwanie na komunikat zwrotny zawierający numer portu TCP do nawiązania połączenia.
 4. < Server > Wykrycie odpowiedniej sekwencji pakietów UDP, poprzez autoryzacje klienta. Autoryzacja wielu klientów w tym samym czasie.
 5. < Server > Jeśli sekwencja pakietów UDP jest niepoprawna, klient nie uzyskuje odpowiedzi oraz po 10 sekundach klient kończy pracę z komunikatem błędu.
 6. <  Klient > Po wykonaniu komunikacji z serwerem, klient rozłącza się i kończy pracę.


>**Komentarz**
>- Server należy uruchomić podając w argumentach numery portów UDP do nasłuchiwania pakietów od klientów.
>- Klienta należy uruchomić podając w argumentach na początku adres serwera oraz w następnych numery portów, na które ma "pukać".
## Protokół serwera
Początkowo serwer na podstawie podanych w argumentach portów UDP uruchamia wątki dla każdego z nich. Każdy wątek nasłuchuje wspomnianego "knockingu". Jeżeli nastąpi autoryzacja klienta, to poprzez UDP serwer wysyła wolny port TCP. Po poprawnym połączeniu klienta z danym portem, port z którego adres został otrzymany zostaje zablokowany  na 10 sekund, by obsłużyć żądanie klienta. Poprzez wspólne informacje wątków o klientach po autoryzacji jednocześnie z obecnie obsługiwanymi żądaniami klient nie może zablokować kilku portów na raz.

## Protokół klienta
Klient na podstawie podanego w argumentach adresu łączy się z serwerem oraz wykonuje "pukanie" na podane porty. Kiedy autoryzacja przbiegnie pomyślnie, łączy się pod uzyskany port przez protokół TCP. Następnym krokiem jest  realizacja żądania klienta, po czym wyświetlenie uzyskanej wiadomości od serwera. Po wykonaniu całej komunikacji z serwerem, klient rozłącza się i kończy pracę.

## Autoryzacja klienta
Autoryzacja klienta następuje w momencie, kiedy w wiadomości nadawanej przez datagramy znajdzie się na początku **symbol '@'**.


## Realizacja żądania klienta
W następstwie autoryzacji, klient wysyła wiadomość do serwera, a ten zwraca ją używając szyfru cezara z przesunięciem o 5 znaków.

### Odpowiedź od serwera z wykorzystaniem szyfru cezara

	BufferedReader reader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));  
	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));  
	String receivedMessage = reader.readLine().toUpperCase();  
	System.out.println("\nRecieived: " + receivedMessage);  
	  
	int alfaLength = 'Z'-'A'+1;  
	int kluczCezara = 5%(alfaLength);  
	char[] tab = receivedMessage.toCharArray();  
	for (int i = 0; i<tab.length; i++){  
	  tab[i] += kluczCezara;  
	  if (tab[i] > 'Z')  
	  tab[i] -=alfaLength;  
	  if (tab[i] < 'A')  
	  tab[i] +=alfaLength;  
	}  
	  
	String hashMessage = String.copyValueOf(tab);  
	  
	writer.write(hashMessage+"\n");  
	writer.flush();
	
|Zmienna                |Opis                          
|----------------|-------------------------------
|`receivedMessage`            |Wiadomość od klienta            |
|`hashMessage`            |Odpowiedź do klienta            |

#
> Autor: **Jakub Jóźwiak**
