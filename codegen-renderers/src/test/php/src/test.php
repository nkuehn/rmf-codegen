<?php
declare(strict_types = 1);
namespace Commercetools;

require __DIR__ . '/../vendor/autoload.php';

use Commercetools\Importer\Client\ClientCredentialsConfig;
use Commercetools\Importer\Client\ClientFactory;
use Commercetools\Importer\Client\Config;
use Commercetools\Importer\Client\MiddlewareFactory;
use Commercetools\Importer\Client\RawTokenProvider;
use Commercetools\Importer\Client\TokenModel;
use Commercetools\Importer\Models\Common\Reference;
use Doctrine\Common\Annotations\AnnotationRegistry;
use JMS\Serializer\Handler\HandlerRegistryInterface;
use JMS\Serializer\Naming\IdenticalPropertyNamingStrategy;
use JMS\Serializer\SerializerBuilder;
use JmsModels\Commercetools\Importer\Models\Common\Address;
use JmsModels\Commercetools\Importer\Models\Common\AddressCollection;
use JmsModels\Commercetools\Importer\Models\Common\AssetCollection;
use JmsModels\Commercetools\Importer\Models\Customer\Customer;
use Monolog\Handler\StreamHandler;
use Monolog\Logger;
use function PHPSTORM_META\type;

AnnotationRegistry::registerLoader('class_exists');

$serializer = SerializerBuilder::create()
//    ->setCacheDir(__DIR__ . "/cache")
    ->setPropertyNamingStrategy(new IdenticalPropertyNamingStrategy())
    ->configureHandlers(function (HandlerRegistryInterface $registry) { $registry->registerSubscribingHandler(new CollectionHandler()); })
    ->build();

$jsonData = '
{ "addresses" : [
{
    "id": "exampleAddress",
    "key": "exampleKey",
    "title": "My address",
    "salutation": "Mr.",
    "firstName": "Example",
    "lastName": "Person",
    "streetName": "Examplary Street",
    "streetNumber": "4711",
    "additionalStreetInfo": "Backhouse",
    "postalCode": "80933",
    "city": "Exemplary City",
    "region": "Exemplary Region",
    "state": "Exemplary State",
    "country": "DE",
    "company": "My Company Name",
    "department": "Sales",
    "building": "Hightower 1",
    "apartment": "247",
    "pOBox": "2471",
    "phone": "+49 89 12345678",
    "mobile": "+49 171 2345678",
    "email": "mail@mail.com",
    "fax": "+49 89 12345679",
    "additionalAddressInfo": "no additional Info",
    "externalId": "Information not needed"
  }]}';


//$object = $serializer->deserialize($jsonData, Customer::class, 'json');

//var_dump($object);

//$c = new AddressCollection();
//$c->set(0, new Customer());
//
//
//$t = new Address();

$assetJson = '[
{
    "sources": [{
      "uri": "https://www.commercetools.de/another.svg",
      "key": "image"
    }],
    "name": {
      "de": "commercetools image",
      "en": "commercetools another"
    }
  },
  {
    "sources": [{
      "uri": "https://www.commercetools.de/ct-logo.svg",
      "key": "vector"
    }],
    "name": {
      "de": "commercetools Logo",
      "en": "commercetools logo"
    }
  }]';

//$object = $serializer->deserialize($assetJson, AssetCollection::class, 'json');
//
//var_dump($object);

$refJson = '{
    "typeId" : "category"
}';
$object = $serializer->deserialize($refJson, Reference::class, 'json');
var_dump($object);
