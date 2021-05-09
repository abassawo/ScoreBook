import SwiftUI
import Combine
import shared

func greet() -> String {
    return Greeting().greeting()
}

class HomeViewModel: ObservableObject {
    @Published var games = [Game]()

    private let repository = AppRepository(database: DatabaseFactory().createDB())

    func fetch() {
//         repository.storeDummyGame(completionHandler: { unit, error in
//             print(result)
//          }
        repository.loadGames(completionHandler: { game, error in
                    if game != nil {
//                         print("Game processed " + game.name())
                        self.games = game!
                    } else {
                        // log error
                    }

                })
    }
}

struct ContentView: View {
    @State private var gameName: String = ""
    @State private var isEditing = false
    @State private var selection: String? = nil

    @State var viewModel = HomeViewModel()

    var body: some View {
        NavigationView {
            List {
                ForEach(viewModel.games, id: \.id) { game in
                    Text("Id value " + game.id())
//                     CharactersListRowView(character: character)
                }
            }
            VStack(spacing: 0) {
                Text("Create a new game").multilineTextAlignment(.center)
                TextField(
                        "Enter a new game",
                         text: $gameName
                    ) { isEditing in
                        self.isEditing = isEditing
                    } onCommit: {
//                         validate(name: gameName)
                    }
                    .autocapitalization(.none)
                    .disableAutocorrection(true)
                    .border(Color(UIColor.separator)).padding()
                NavigationLink(destination: Text("Second View"), tag: "Second", selection: $selection) { EmptyView() }
                NavigationLink(destination: Text("Third View"), tag: "Third", selection: $selection) { EmptyView() }


                Button("Start New Game") {
                    self.selection = "Second"
                }
            }
            .navigationBarTitle("Score Book")
            Spacer()
        }.padding(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
