import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack 
{
    // Inner class to represent a Card with value and type
    private class Card 
    {
        String value; // The value of the card (e.g., "A", "2", ..., "K")
        String type;  // The type of the card (e.g., "C" for Clubs, "D" for Diamonds, etc.)

        Card(String value, String type) 
        {
            this.value = value;
            this.type = type;
        }

        // Returns a string representation of the card
        public String toString() 
        {
            return value + "-" + type;
        }

        // Returns the integer value of the card
        public int getValue() 
        {
            if ("AJQK".contains(value)) 
            { // For A, J, Q, K
                if (value.equals("A")) 
                {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); // For cards 2-10
        }

        // Checks if the card is an Ace
        public boolean isAce() 
        {
            return value.equals("A");
        }

        // Returns the image path of the card
        public String getImagePath() 
        {
            return "./cards/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck; // List to hold the deck of cards
    Random random = new Random(); // Random object for shuffling the deck

    // Dealer
    Card hiddenCard; // Dealer's hidden card
    ArrayList<Card> dealerHand; // List to hold dealer's hand
    int dealerSum; // Sum of dealer's hand values
    int dealerAceCount; // Count of Aces in dealer's hand

    // Player
    ArrayList<Card> playerHand; // List to hold player's hand
    int playerSum; // Sum of player's hand values
    int playerAceCount; // Count of Aces in player's hand

    // Window dimensions
    int boardWidth = 800;
    int boardHeight = boardWidth;

    // Card dimensions
    int cardWidth = 100; // Ratio should be 1/1.4
    int cardHeight = 140;

    // Game statistics
    int winCount = 0;
    int loseCount = 0;
    int tieCount = 0;
    int total_game = 0;
    double winRate = 0.0;

    // Player's money and betting
    int playerMoney = 10000;
    int RoundBet = 0;

    // GUI components
    JFrame frame = new JFrame("Black Jack");
    JPanel cardPanel = new JPanel(new CardLayout());
    JPanel gamePanel = new JPanel() 
    {
        @Override
        public void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            try 
            {
                // Draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) 
                {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 50, 100, cardWidth, cardHeight, null);
                
                // Draw text labels
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Dealer", 60, 90);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Player", 60, 525);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Deck", 345, 470);

                // Showing Deck
                for (int i = 0; i < 10; i++)
                {
                    g.drawImage(hiddenCardImg, 320-i, 310-i, cardWidth, cardHeight, null);
                }

                // Draw dealer's hand
                for (int i = 0; i < dealerHand.size(); i++) 
                {
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth + 55 + (cardWidth + 5) * i, 100, cardWidth, cardHeight, null);
                }
                // Draw player's hand
                for (int i = 0; i < playerHand.size(); i++) 
                {
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 50 + (cardWidth + 5) * i, 535, cardWidth, cardHeight, null);
                }
                
                // Display betting amount
                g.drawString("betting: " + RoundBet, 180, 525);

                // Game over condition
                if ((playerMoney == 0) && (RoundBet == 0))
                {
                    JOptionPane.showMessageDialog(frame, "Kicked from game", "Game Over", JOptionPane.ERROR_MESSAGE);
                    frame.dispose();    // Optionally reset the game
                }

                // Check if stay button is disabled
                if (!stayButton.isEnabled()) 
                {
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY: ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String message = "";
                    if ((playerSum > 21))
                    {
                        message = "Bust You Lose";
                        loseCount++;
                        RoundBet = 0;
                    } 
                    if ((dealerSum > 21))
                    {
                        message = "Bust You Win";
                        loseCount++;
                        RoundBet = 0;
                    } 
                    else if ((dealerSum < 21) && (playerSum < dealerSum))
                    {
                        message = "You Lose!";
                        loseCount++;
                        RoundBet = 0;
                    }
                    else if ((dealerSum < 21) && (playerSum > dealerSum)) 
                    {
                        message = "You Win!";
                        winCount++;
                        playerMoney += RoundBet*2;
                        RoundBet = 0;
                    }
                    else if ((dealerSum > 21) && (playerSum < 21))
                    {
                        message = "You Win!";
                        winCount++;
                        playerMoney += RoundBet*2;
                        RoundBet = 0;
                    }
                    else if (playerSum == dealerSum) 
                    {
                        message = "Tie!";
                        tieCount++;
                    }
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.drawString(message, 300, 500);

                    retryButton.setVisible(true);
                    quitButton.setVisible(true);
                }
                
                // Display win rate and stats
                total_game = winCount + loseCount + tieCount;
                winRate = (total_game == 0)? 0.0 : (double)(winCount) / (double)(total_game) * 100;
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("Wins: " + winCount, boardWidth - 300, 40);
                g.drawString("Loses: " + loseCount, boardWidth - 200, 40);
                g.drawString("Ties: " + tieCount, boardWidth - 100, 40);
                g.drawString("Win Rate: " + winRate + "%", boardWidth - 200, 70);
                g.drawString("Player Money: " + playerMoney, 60, 710);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    };


    // Panel to hold buttons
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton retryButton = new JButton("Retry");
    JButton quitButton = new JButton("Quit");


    // Constructor to initialize the game
    BlackJack() 
    {
        // SET GUI
        // Start screen setup
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setBackground(new Color(0, 0, 0));

        // Title label
        JLabel titleLabel = new JLabel("Black Jack");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        // Start and exit buttons
        JButton startButton = new JButton("Start");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFocusable(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setFocusable(false);

        // Add components to start panel
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(titleLabel);
        startPanel.add(Box.createRigidArea(new Dimension(0, 200)));
        startPanel.add(startButton);
        startPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        startPanel.add(exitButton);
        startPanel.add(Box.createVerticalGlue());

        // Add start and game panels to card panel
        cardPanel.add(startPanel, "start");
        cardPanel.add(gamePanel, "game");

        // Frame setup
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(cardPanel);

        // Game panel setup
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(0, 0, 0));

        // Button panel setup
        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        retryButton.setFocusable(false);
        buttonPanel.add(retryButton);
        quitButton.setFocusable(false);
        buttonPanel.add(quitButton);

        gamePanel.add(buttonPanel, BorderLayout.SOUTH);

        retryButton.setVisible(false);
        quitButton.setVisible(false);

        // Start button action listener
        startButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, "game");
                startGame();
                gamePanel.repaint();

                // Set Bet
                RoundBet = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your bet amount: ")); // Get bet amount
                while (playerMoney < RoundBet)
                {
                    JOptionPane.showMessageDialog(frame, "Not enough Money", "", JOptionPane.ERROR_MESSAGE);
                    RoundBet = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your bet amount: "));
                }
                playerMoney -= RoundBet;  
            }
        });

        // Exit button action listener
        exitButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                System.exit(0);
            }
        });

        // Hit button action listener
        hitButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() > 21) // Handle ace reduction
                { 
                    hitButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        });

        // Stay button action listener
        stayButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                // Dealer's turn to draw cards
                while (dealerSum < 17) 
                {
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }
                gamePanel.repaint();
            }
        });

        // Retry button action listener
        retryButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                startGame();
                gamePanel.repaint();
                retryButton.setVisible(false);
                quitButton.setVisible(false);

                // Set Bet
                RoundBet = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your bet amount: ")); // Get bet amount
                while (playerMoney < RoundBet)
                {
                    JOptionPane.showMessageDialog(frame, "Not enough Money", "", JOptionPane.ERROR_MESSAGE);
                    RoundBet = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter your bet amount: "));
                }
                playerMoney -= RoundBet; 
            }
        });

        // Quit button action listener
        quitButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                CardLayout cl = (CardLayout) (cardPanel.getLayout());
                cl.show(cardPanel, "start");
                retryButton.setVisible(false);
                quitButton.setVisible(false);
                winCount = 0;
                loseCount = 0;
                tieCount = 0;
                winRate = 0;
                total_game = 0;
                playerMoney = 10000;
            }
        });
    }



    // Game system
    // Method to start the game
    public void startGame() 
    {
        // Build and shuffle the deck
        buildDeck();
        shuffleDeck();

        // Initialize dealer's hand
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1); // Remove card at last index
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        // Initialize player's hand
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) 
        {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

        System.out.println("PLAYER: ");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);

        hitButton.setEnabled(true);
        stayButton.setEnabled(true);
        retryButton.setVisible(false);
        quitButton.setVisible(false);
    }

    // Method to build the deck of cards
    public void buildDeck() 
    {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (int i = 0; i < types.length; i++) 
        {
            for (int j = 0; j < values.length; j++) 
            {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);
    }

    // Method to shuffle the deck
    public void shuffleDeck() 
    {
        for (int i = 0; i < deck.size(); i++) 
        {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }

    // Method to reduce the value of Aces in player's hand if necessary
    public int reducePlayerAce() 
    {
        while (playerSum > 21 && playerAceCount > 0) 
        {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    // Method to reduce the value of Aces in dealer's hand if necessary
    public int reduceDealerAce() 
    {
        while (dealerSum > 21 && dealerAceCount > 0) 
        {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    // Main method to run the game
    public static void main(String[] args) 
    {
        new BlackJack();
    }
}
