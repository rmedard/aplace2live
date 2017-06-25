/**
 * Created by medard on 19.06.17.
 */

$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})

$('#confirmParticipationId').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var activiteSlug = button.data('activiteslug') // Extract info from data-* attributes
    var activiteTitle = button.data('activitetitle')
    console.log(activiteSlug)
    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
    var modal = $(this)
    modal.find('.modal-body p span#activiteTitleId').text(activiteTitle)
    modal.find('.modal-body input#activiteSlugId').val(activiteSlug)
})