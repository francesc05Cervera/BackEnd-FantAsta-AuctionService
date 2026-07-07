package com.example.fantasta.auction_service.service;
import com.example.fantasta.auction_service.repository.AuctionParticipantRepository;
import com.example.fantasta.auction_service.dto.AuthUserResponse;
import com.example.fantasta.auction_service.entity.Auction;
import com.example.fantasta.auction_service.entity.AuctionParticipant;
import com.example.fantasta.auction_service.exception.TokenException;
import com.example.fantasta.auction_service.exception.NotFoundException;
import com.example.fantasta.auction_service.exception.ForbiddenException;
import com.example.fantasta.auction_service.repository.AuctionRepository;
import com.example.fantasta.auction_service.client.AuthServiceClient;


import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionParticipantService 
{
    private final AuctionParticipantRepository auctionParticipantRepository;
    private final AuctionRepository auctionRepository; 
    private final AuthServiceClient authServiceClient;
    public AuctionParticipantService(AuctionParticipantRepository auctionParticipantRepository, AuctionRepository auctionRepository, AuthServiceClient authServiceClient) 
    {
        this.auctionParticipantRepository = auctionParticipantRepository;
        this.auctionRepository = auctionRepository;
        this.authServiceClient = authServiceClient;
    }
   
    /*
        Metodo di supporto per verificare se un utente ha già partecipato a un'asta specifica.
        @param auctionId: ID dell'asta da verificare.
        @param userId: ID dell'utente da verificare.       
        @return true se l'utente ha già partecipato all'asta, false altrimenti.
        
    */
    private boolean isUserAlreadyJoined(int auctionId, Long userId) 
    {
        return auctionParticipantRepository.existsByAuctionIdAndUserId(auctionId, userId);
    }

    /*
        Metodo richiamato dal controller per consentire a un utente di partecipare a un'asta.
        @param auctionId: ID dell'asta a cui l'utente desidera partecipare.
        @param userId: ID dell'utente che desidera partecipare all'asta.
        @return true se l'utente è stato aggiunto con successo all'asta, false se l'utente ha già partecipato all'asta.
        
    */
    public boolean join(int auctionId, Long userId) 
    {
        if (isUserAlreadyJoined(auctionId, userId)) 
        {
            return false; 
        }

        AuctionParticipant participant = new AuctionParticipant();
        participant.setAuctionId(auctionId);   
        participant.setUserId(userId);
        participant.setJoinedAt(java.time.LocalDateTime.now());
        auctionParticipantRepository.save(participant);
        return true;
    }

    /*
        Metodo programmato per la pulizia dei partecipanti alle aste.
    */
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void cleanupOldParticipants() 
    {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(5);
        auctionParticipantRepository.deleteByJoinedAtBefore(cutoff);
    }

    /*
        Metodo per ottenere i partecipanti a un'asta specifica.
        @param auctionId: ID dell'asta per cui ottenere i partecipanti.
        @return Lista dei partecipanti all'asta.
    */
    public List<AuctionParticipant> getParticipant(int auctionId) 
    {
        return auctionParticipantRepository.findByAuctionId(auctionId);
    }

    /*
        Metodo per approvare un partecipante a un'asta.
        @param authorizationHeader: Header di autorizzazione contenente il token dell'utente autenticato.
        @param auctionId: ID dell'asta per cui approvare il partecipante.
        @param participantUserId: ID dell'utente che desidera essere approvato.
        @throws TokenException: Se il token di autorizzazione non è valido o scaduto.
        @throws NotFoundException: Se l'asta o il partecipante non sono stati trovati.
        @throws ForbiddenException: Se l'utente autenticato non è il proprietario dell'asta.
    */
    public void approveParticipant(String authorizationHeader, int auctionId, Long participantUserId)
        throws TokenException, NotFoundException, ForbiddenException {

    AuthUserResponse user = authServiceClient.getAuthenticatedUser(authorizationHeader);

    if (user == null) {
        throw new TokenException("Invalid or expired token");
    }

    Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new NotFoundException("Auction with ID " + auctionId + " not found"));

    if (!auction.getCreatorUserId().equals(user.getId())) {
        throw new ForbiddenException("Only the auction owner can approve participants.");
    }

    AuctionParticipant participant = auctionParticipantRepository
            .findByAuctionIdAndUserId(auctionId, participantUserId)
            .orElseThrow(() -> new NotFoundException("Participant not found for this auction"));

    participant.setApproved(true);
    auctionParticipantRepository.save(participant);
}
}
